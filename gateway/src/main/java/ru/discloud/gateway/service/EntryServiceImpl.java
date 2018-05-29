package ru.discloud.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.domain.Entry;
import ru.discloud.gateway.exception.ServiceResponseParsingException;
import ru.discloud.gateway.request.service.AuthRequestService;
import ru.discloud.gateway.request.service.ServiceEnum;
import ru.discloud.gateway.request.store.FileStoreRequestService;
import ru.discloud.gateway.web.model.EntryRequest;

import java.io.IOException;
import java.util.List;

@Service
public class EntryServiceImpl implements EntryService {
  private final ObjectMapper mapper = new ObjectMapper();
  private final FileStoreRequestService fileStore;

  @Autowired
  public EntryServiceImpl(FileStoreRequestService fileStore) {
    this.fileStore = fileStore;
  }

  @Override
  public Mono<List<Entry>> getEntries(Long userId) {
    return fileStore.request("GET", String.format("/entry/%d/entry", userId))
        .doOnSuccess(response -> AuthRequestService.checkServiceResponse(ServiceEnum.FILE, response))
        .map(this::mapResponseToEntryList);
  }

  @Override
  public Mono<Entry> getEntryByUuid(Long userId, String uuid) {
    return fileStore.request("GET", String.format("/entry/%d/entry/%s", userId, uuid))
        .doOnSuccess(response -> AuthRequestService.checkServiceResponse(ServiceEnum.FILE, response))
        .map(this::mapResponseToEntry);
  }

  @Override
  public Mono<Entry> createEntry(Long userId, EntryRequest request) throws JsonProcessingException {
    new Entry(request);
    return fileStore
        .request("POST", String.format("/entry/%d/entry", userId), null,
            mapper.writeValueAsString(new Entry(request).getCoreEntryRequest()))
        .doOnSuccess(response -> AuthRequestService.checkServiceResponse(ServiceEnum.FILE, response))
        .map(this::mapResponseToEntry);
  }

  @Override
  public Mono<Entry> updateEntryByUuid(Long userId, EntryRequest request) {
    return null;
  }

  @Override
  public Mono<Void> deleteEntryByUuid(Long userId, String uuid) {
    return fileStore.request("DELETE", String.format("/entry/%d/entry/%s", userId, uuid))
        .doOnSuccess(response -> AuthRequestService.checkServiceResponse(ServiceEnum.FILE, response))
        .then();
  }

  private List<Entry> mapResponseToEntryList(Response response) {
    try {
      return mapper.readValue(response.getResponseBody(), new TypeReference<List<Entry>>() {
      });
    } catch (IOException ex) {
      throw new ServiceResponseParsingException(ex.getMessage());
    }
  }

  private Entry mapResponseToEntry(Response response) {
    try {
      return mapper.readValue(response.getResponseBody(), Entry.class);
    } catch (IOException ex) {
      throw new ServiceResponseParsingException(ex.getMessage());
    }
  }
}
