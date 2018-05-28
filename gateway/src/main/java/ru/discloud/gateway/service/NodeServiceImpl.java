package ru.discloud.gateway.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.domain.Node;
import ru.discloud.gateway.exception.ServiceResponseParsingException;
import ru.discloud.gateway.request.service.AuthRequestService;
import ru.discloud.gateway.request.service.ServiceEnum;
import ru.discloud.gateway.request.store.FileStoreRequestService;

import java.io.IOException;
import java.util.List;

@Service
public class NodeServiceImpl implements NodeService {
  private final ObjectMapper mapper = new ObjectMapper();
  private final FileStoreRequestService fileStore;

  @Autowired
  public NodeServiceImpl(FileStoreRequestService fileStore) {
    this.fileStore = fileStore;
  }

  @Override
  public Mono<List<Node>> getNodes() {
    return fileStore.request("GET", "/node/global")
        .doOnSuccess(response -> AuthRequestService.checkServiceResponse(ServiceEnum.FILE, response))
        .map(this::mapResponseToNodeList);
  }

  private List<Node> mapResponseToNodeList(Response response) {
    try {
      return mapper.readValue(response.getResponseBody(), new TypeReference<List<Node>>() {});
    } catch (IOException ex) {
      throw new ServiceResponseParsingException(ex.getMessage());
    }
  }
}
