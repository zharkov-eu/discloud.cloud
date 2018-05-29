package ru.discloud.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.domain.Entry;
import ru.discloud.gateway.web.model.EntryRequest;

import java.util.List;

public interface EntryService {
  Mono<List<Entry>> getEntries(Long userId);

  Mono<Entry> getEntryByUuid(Long userId, String uuid);

  Mono<Entry> createEntry(Long userId, EntryRequest request) throws JsonProcessingException;

  Mono<Entry> updateEntryByUuid(Long userId, EntryRequest request);

  Mono<Void> deleteEntryByUuid(Long userId, String uuid);
}
