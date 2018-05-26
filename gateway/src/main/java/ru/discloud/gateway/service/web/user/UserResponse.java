package ru.discloud.gateway.service.web.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import ru.discloud.shared.UserPrivileges;

@Getter
public class UserResponse implements ru.discloud.shared.web.user.UserResponse {
  private final Long id;
  private final String email;
  private final String phone;
  private final String username;
  private final UserPrivileges userPrivileges;
  private final Long quota;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public UserResponse(@JsonProperty("id") Long id,
                      @JsonProperty("email") String email,
                      @JsonProperty("phone") String phone,
                      @JsonProperty("username") String username,
                      @JsonProperty("userPrivileges") UserPrivileges userPrivileges,
                      @JsonProperty("quota") Long quota) {
    this.id = id;
    this.email = email;
    this.phone = phone;
    this.username = username;
    this.userPrivileges = userPrivileges;
    this.quota = quota;
  }
}
