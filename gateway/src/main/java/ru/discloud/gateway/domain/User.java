package ru.discloud.gateway.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.discloud.shared.UserPrivileges;

@Data
@Accessors(chain = true)
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private UserPrivileges privileges;
    private Long quota;
}
