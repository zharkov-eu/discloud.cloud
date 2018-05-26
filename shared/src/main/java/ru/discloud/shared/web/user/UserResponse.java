package ru.discloud.shared.web.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.discloud.shared.UserPrivileges;

public interface UserResponse {
  @JsonProperty("id")
  Long getId();

  @JsonProperty("email")
  String getEmail();

  @JsonProperty("phone")
  String getPhone();

  @JsonProperty("username")
  String getUsername();

  @JsonProperty("userPrivileges")
  UserPrivileges getUserPrivileges();

  @JsonProperty("quota")
  Long getQuota();
}
