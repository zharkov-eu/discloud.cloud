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
import ru.discloud.gateway.request.service.AuthRequestService;
import ru.discloud.gateway.request.service.ServiceEnum;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class FileStoreRequestService {
  private final ObjectMapper mapper = new ObjectMapper();
  private final AuthRequestService requestService;
  private List<FileNode> fileNodes;
  private final AtomicBoolean fileNodesCheck = new AtomicBoolean(false);

  @Autowired
  public FileStoreRequestService(AuthRequestService requestService) throws IOException {
    this.requestService = requestService;
    this.fileNodes = mapper.readValue(
        GatewayApplication.class.getClassLoader().getResourceAsStream("filenode.json"),
        new TypeReference<List<FileNode>>() {}
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
        .switchIfEmpty(this.refreshFileNodes().map(target -> target))
        .doOnSuccess((target) -> {
          if (target == null) throw new MasterNodeNotExistsException();
        })
        .flatMap(target -> Mono
            .fromFuture(requestService.request(ServiceEnum.FILE, method, target.getBaseUrl() + url, queryParams, body))
        );
  }

  @Nullable
  private FileNode getMasterFileNode() {
    return fileNodes.stream().filter(it -> it.getRole() == FileNodeRoleEnum.MASTER).findFirst().orElse(null);
  }

  private Mono<@Nullable FileNode> refreshFileNodes() {
    synchronized (fileNodesCheck) {
      if (this.getMasterFileNode() != null) return Mono.fromCallable(this::getMasterFileNode);

      if (!fileNodesCheck.get()) {
        fileNodesCheck.set(true);
        return Flux.fromIterable(fileNodes)
            .flatMap(it -> Mono.fromFuture(
                requestService.request(ServiceEnum.FILE, "GET", it.getBaseUrl() + "/node")
                      .exceptionally(e -> null)
            ))
            .filter(response -> response != null && response.getStatusCode() == 200)
            .next()
            .map(response -> {
              try {
                fileNodes = mapper.readValue(response.getResponseBody(), new TypeReference<List<FileNode>>() {
                });
                return this.getMasterFileNode();
              } catch (IOException e) {
                log.error("Can't parse node response");
                return null;
              }
            });

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
