package ru.discloud.gateway.service.web.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserResponse implements ru.discloud.shared.web.auth.UserResponse {
  private final Long id;
  private final String username;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public UserResponse(@JsonProperty("id") Long id,
                      @JsonProperty("username") String username) {
    this.id = id;
    this.username = username;
  }
}
