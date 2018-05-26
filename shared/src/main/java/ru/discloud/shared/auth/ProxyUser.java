package ru.discloud.shared.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProxyUser {
  private String username;
  private String password;
}
