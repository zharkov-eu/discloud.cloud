package ru.discloud.shared.web.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface UserResponse {
  @JsonProperty("id")
  Long getId();

  @JsonProperty("email")
  String getUsername();
}
