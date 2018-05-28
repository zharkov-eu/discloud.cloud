package ru.discloud.gateway.web.model;

import ru.discloud.gateway.domain.Group;

public class GroupResponse extends ru.discloud.shared.web.core.GroupResponse {
  public GroupResponse(Group group) {
    this.id = group.getId();
    this.name = group.getName();
    this.system = group.getSystem();
  }
}
