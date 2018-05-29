package ru.discloud.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.domain.Group;
import ru.discloud.shared.web.core.GroupRequest;

import java.util.List;

public interface GroupService {
  Mono<List<Group>> getGroups();

  Mono<Group> getGroupById(Integer id);

  Mono<Group> createGroup(GroupRequest request) throws JsonProcessingException;

  Mono<Group> updateGroup(Integer id, GroupRequest request) throws JsonProcessingException;

  Mono<Void> deleteGroup(Integer id);
}
