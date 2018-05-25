package ru.discloud.gateway.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.discloud.gateway.domain.User;
import ru.discloud.shared.UserPrivileges;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
  private Long id;
  private String email;
  private String phone;
  private String username;
  private UserPrivileges privileges;

  public UserResponse(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.phone = user.getPhone();
    this.username = user.getUsername();
    this.privileges = user.getPrivileges();
  }
}
