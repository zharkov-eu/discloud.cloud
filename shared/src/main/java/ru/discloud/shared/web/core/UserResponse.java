package ru.discloud.shared.web.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
  @JsonProperty("id")
  protected String id;

  @JsonProperty("username")
  protected String username;

  @JsonProperty("group")
  protected List<String> group;
}
