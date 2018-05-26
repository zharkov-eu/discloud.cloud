package ru.discloud.shared.web.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface UserRequest {
  @JsonProperty("id")
  Long getId();

  @JsonProperty("username")
  String getUsername();

  @JsonProperty("password")
  String getPassword();
}
