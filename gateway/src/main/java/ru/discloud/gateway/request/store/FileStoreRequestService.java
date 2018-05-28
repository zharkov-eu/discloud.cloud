package ru.discloud.gateway.request.store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Response;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.GatewayApplication;
import ru.discloud.gateway.domain.Node;
import ru.discloud.gateway.exception.ServiceUnavailableException;
import ru.discloud.gateway.request.service.AuthRequestService;
import ru.discloud.gateway.request.service.ServiceEnum;
import ru.discloud.shared.web.core.NodeRoleEnum;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class FileStoreRequestService {
  private final ObjectMapper mapper = new ObjectMapper();
  private final AuthRequestService requestService;
  private List<Node> fileNodes;
  private final AtomicBoolean fileNodesCheck = new AtomicBoolean(false);

  @Autowired
  public FileStoreRequestService(AuthRequestService requestService) throws IOException {
    this.requestService = requestService;
    this.fileNodes = mapper.readValue(
        GatewayApplication.class.getClassLoader().getResourceAsStream("filenode.json"),
        new TypeReference<List<Node>>() {
        }
    );
  }

  public Mono<Response> request(String method, String url) {
    return nodeRequest(method, url, null, null);
  }

  public Mono<Response> request(String method, String url,
                                Map<String, List<String>> queryParams) {
    return nodeRequest(method, url, queryParams, null);
  }

  public Mono<Response> request(String method, String url,
                                Map<String, List<String>> queryParams, String body) {
    return nodeRequest(method, url, queryParams, body);
  }

  private Mono<Response> nodeRequest(String method, String url,
                                     Map<String, List<String>> queryParams, String body) {
    return Mono.fromCallable(this::getMasterFileNode)
        .switchIfEmpty(this.refreshFileNodes(false).map(target -> target))
        .doOnSuccess((target) -> {
          if (target == null) throw new MasterNodeNotExistsException();
        })
        .flatMap(target -> Mono
            .fromFuture(requestService.request(ServiceEnum.FILE, method, target.getBaseUrl() + url, queryParams, body)
                .exceptionally(ex -> {
                  if (ex.getCause() instanceof ServiceUnavailableException) return null;
                  else throw new RuntimeException(ex);
                }))
        )
        .switchIfEmpty(this.refreshFileNodes(true)
            .flatMap(target -> {
              if (target == null) throw new MasterNodeNotExistsException();
              else return Mono.fromFuture(
                  requestService.request(ServiceEnum.FILE, method, target.getBaseUrl() + url, queryParams, body));
            })
        );
  }

  @Nullable
  private Node getMasterFileNode() {
    return fileNodes.stream().filter(it -> it.getRole() == NodeRoleEnum.MASTER).findFirst().orElse(null);
  }

  private Mono<@Nullable Node> refreshFileNodes(boolean forceUpdate) {
    synchronized (fileNodesCheck) {
      if (!forceUpdate && this.getMasterFileNode() != null) return Mono.fromCallable(this::getMasterFileNode);

      if (!fileNodesCheck.get()) {
        fileNodesCheck.set(true);
        return Flux.fromIterable(fileNodes)
            .flatMap(it -> Mono.fromFuture(
                requestService.request(ServiceEnum.FILE, "GET", it.getBaseUrl() + "/node/global")
                    .exceptionally(e -> {
                      log.error(e.getMessage());
                      return null;
                    })
            ))
            .filter(response -> response != null && response.getStatusCode() == 200)
            .next()
            .map(response -> {
              try {
                fileNodes = mapper.readValue(response.getResponseBody(), new TypeReference<List<Node>>() {
                });
                return this.getMasterFileNode();
              } catch (IOException e) {
                log.error("Can't parse node response");
                return null;
              }
            })
            .doOnTerminate(() -> fileNodesCheck.set(false));

      } else {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          log.error("RefreshFileNode: main thread interrupted");
        }

        return Mono.fromCallable(this::getMasterFileNode);
      }
    }
  }
}
