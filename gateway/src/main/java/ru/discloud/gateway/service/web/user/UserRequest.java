package ru.discloud.gateway.service.web.user;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.discloud.shared.UserPrivileges;

@Data
@Accessors(chain = true)
public class UserRequest implements ru.discloud.shared.web.user.UserRequest {
  private Long id;
  private String email;
  private String phone;
  private String username;
  private UserPrivileges userPrivileges;
  private Long quota;
}
