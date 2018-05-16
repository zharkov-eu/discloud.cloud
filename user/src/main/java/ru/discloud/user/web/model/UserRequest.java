package ru.discloud.user.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class UserRequest {
    @Email(message = "Provided email isn't valid")
    private String email;

    @NotEmpty(message = "Phone not provided")
    private String phone;

    private String username;
}
