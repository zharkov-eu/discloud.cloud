package ru.discloud.shared.web.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRequest {
    private String email;
    private String phone;
    private String username;
    private String userPrivileges;
    private Long quota;
}
