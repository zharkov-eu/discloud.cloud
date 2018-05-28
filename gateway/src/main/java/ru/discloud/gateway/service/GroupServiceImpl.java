package ru.discloud.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.domain.Group;
import ru.discloud.gateway.exception.ServiceResponseParsingException;
import ru.discloud.gateway.request.service.AuthRequestService;
import ru.discloud.gateway.request.service.ServiceEnum;
import ru.discloud.gateway.request.store.FileStoreRequestService;
import ru.discloud.shared.web.core.GroupRequest;

import java.io.IOException;

@Service
public class GroupServiceImpl implements GroupService {
  private final ObjectMapper mapper = new ObjectMapper();
  private final FileStoreRequestService fileStore;

  @Autowired
  public GroupServiceImpl(FileStoreRequestService fileStore) {
    this.fileStore = fileStore;
  }

  @Override
  public Mono<Group> getGroupById(Integer id) {
    return fileStore.request("GET", "/group/" + id)
        .doOnSuccess(response -> AuthRequestService.checkServiceResponse(ServiceEnum.FILE, response))
        .map(this::mapResponseToGroup);
  }

  @Override
  public Mono<Group> createGroup(GroupRequest request) {
    return null;
  }

  @Override
  public Mono<Group> updateGroup(Integer id, GroupRequest request) {
    return null;
  }

  @Override
  public Mono<Void> deleteGroup(Integer id) {
    return fileStore.request("DELETE", "/group/" + id)
        .doOnSuccess(response -> AuthRequestService.checkServiceResponse(ServiceEnum.FILE, response))
        .then();
  }

  private Group mapResponseToGroup(Response response) {
    try {
      return mapper.readValue(response.getResponseBody(), Group.class);
    } catch (IOException ex) {
      throw new ServiceResponseParsingException(ex.getMessage());
    }
  }
}
