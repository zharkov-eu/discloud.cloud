package ru.discloud.user.web.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.discloud.shared.MemberOf;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class UserRequest implements ru.discloud.shared.web.User.UserRequest {
    @Email(message = "Provided email isn't valid")
    private String email;

    @NotEmpty(message = "Phone not provided")
    private String phone;

    @NotEmpty(message = "Username not provided")
    private String username;

    @MemberOf(value = "user,admin", message = "userPrivileges isn't recognized")
    private String userPrivileges;

    @NotNull(message = "Quota not provided")
    private Long quota;
}
