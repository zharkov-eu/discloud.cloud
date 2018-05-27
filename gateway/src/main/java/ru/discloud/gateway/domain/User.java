package ru.discloud.gateway.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.discloud.shared.UserPrivileges;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class User {
  private Long id;
  private String username;
  private String email;
  private List<String> group;
  private String phone;
  private String password;
  private UserPrivileges privileges;
  private Long quota;
}