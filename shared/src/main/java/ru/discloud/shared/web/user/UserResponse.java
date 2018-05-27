package ru.discloud.shared.web.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.discloud.shared.UserPrivileges;

@Data
public class UserResponse {
  @JsonProperty("id")
  protected Long id;

  @JsonProperty("email")
  protected String email;

  @JsonProperty("phone")
  protected String phone;

  @JsonProperty("username")
  protected String username;

  @JsonProperty("privileges")
  protected UserPrivileges privileges;

  @JsonProperty("quota")
  protected Long quota;
}
