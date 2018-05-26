package ru.discloud.gateway.service.web.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRequest implements ru.discloud.shared.web.auth.UserRequest {
  private Long id;
  private String username;
  private String password;
}
