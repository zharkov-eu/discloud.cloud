package ru.discloud.gateway.service;

import reactor.core.publisher.Mono;
import ru.discloud.gateway.domain.Group;
import ru.discloud.shared.web.core.GroupRequest;

public interface GroupService {
  Mono<Group> getGroupById(Integer id);

  Mono<Group> createGroup(GroupRequest request);

  Mono<Group> updateGroup(Integer id, GroupRequest request);

  Mono<Void> deleteGroup(Integer id);
}
