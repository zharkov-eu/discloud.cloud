package ru.discloud.gateway.request.store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.request.service.AuthRequestService;
import ru.discloud.gateway.request.service.ServiceEnum;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
public class FileStoreRequestService {
  private final ObjectMapper mapper = new ObjectMapper();
  private final AuthRequestService requestService;
  private Map<String, FileNode> fileNodes;

  @Autowired
  public FileStoreRequestService(AuthRequestService requestService) {
    this.requestService = requestService;
    this.fileNodes = new HashMap<>();
  }

  public CompletableFuture<Response> request(String method, String url) {
    return requestService.request(ServiceEnum.FILE, method, url);
  }

  public CompletableFuture<Response> request(String method, String url,
                                             Map<String, List<String>> queryParams) {
    return requestService.request(ServiceEnum.FILE, method, url, queryParams);
  }

  public CompletableFuture<Response> request(String method, String url,
                                             Map<String, List<String>> queryParams, String body) {
    return requestService.request(ServiceEnum.FILE, method, url, queryParams, body);
  }

  private void refreshFileNodes() {
    Flux.fromIterable(fileNodes.values())
        .flatMap(it -> {
          String url = it.getProtocol() + "://" + (it.getHost() != null ? it.getHost() : it.getIpv4()) + ":" + it.getPort();
          return Mono.fromFuture(requestService.request(ServiceEnum.FILE, "GET", url));
        })
        .onErrorResume(ex -> {
          log.error(ex.getMessage());
          return null;
        })
        .filter(response -> response != null && response.getStatusCode() == 200)
        .take(1)
        .subscribe(response -> {
          try {
            Map<String, FileNode> fileNodesActive = new HashMap<>();
            List<FileNode> nodes = mapper.readValue(response.getResponseBody(), new TypeReference<List<FileNode>>() {});
            for (FileNode node : nodes) {
              fileNodesActive.put(node.getUid(), node);
            }
            fileNodes = fileNodesActive;
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }
}
