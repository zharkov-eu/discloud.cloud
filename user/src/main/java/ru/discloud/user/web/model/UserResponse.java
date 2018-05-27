package ru.discloud.user.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.discloud.user.domain.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse  extends ru.discloud.shared.web.user.UserResponse {
  public UserResponse(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.phone = user.getPhone();
    this.username = user.getUsername();
    this.privileges = user.getPrivileges();
    this.quota = user.getQuota();
  }
}
