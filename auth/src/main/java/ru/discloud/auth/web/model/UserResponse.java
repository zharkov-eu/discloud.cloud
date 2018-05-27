package ru.discloud.auth.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.discloud.auth.domain.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends ru.discloud.shared.web.auth.UserResponse {

  public UserResponse(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
  }
}
