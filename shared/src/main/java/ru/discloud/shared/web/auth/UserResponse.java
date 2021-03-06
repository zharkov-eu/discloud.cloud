package ru.discloud.shared.web.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserResponse {
  @JsonProperty("id")
  protected Long id;

  @JsonProperty("username")
  protected String username;
}
