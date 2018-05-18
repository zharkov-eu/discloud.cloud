package ru.discloud.user.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ru.discloud.shared.MemberOf;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserRequest extends ru.discloud.shared.web.user.UserRequest {
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
