package ru.discloud.gateway.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.service.EntryService;
import ru.discloud.gateway.web.model.EntryRequest;
import ru.discloud.gateway.web.model.EntryResponse;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/entry")
public class EntryController {
  private final EntryService entryService;

  @Autowired
  public EntryController(EntryService entryService) {
    this.entryService = entryService;
  }

  @PostMapping(path = "/{userId}")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<EntryResponse> createEntry(@Valid @RequestBody EntryRequest entryRequest,
                                         @PathVariable Long userId) throws ValidationException, JsonProcessingException {
    return entryService.createEntry(userId, entryRequest).map(EntryResponse::new);
  }

  @GetMapping(path = "/{userId}/")
  public Mono<List<EntryResponse>> getEntries(@PathVariable Long userId) {
    return entryService.getEntries(userId).map(entries -> entries.stream().map(EntryResponse::new).collect(Collectors.toList()));
  }

  @GetMapping(path = "/{userId}/uuid/{uuid}")
  public Mono<EntryResponse> getGroup(@PathVariable Long userId, @PathVariable String uuid) {
    return entryService.getEntryByUuid(userId, uuid).map(EntryResponse::new);
  }

  @DeleteMapping(path = "/{userId}/uuid/{uuid}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteGroup(@PathVariable Long userId, @PathVariable String uuid) {
    return entryService.deleteEntryByUuid(userId, uuid);
  }
}
