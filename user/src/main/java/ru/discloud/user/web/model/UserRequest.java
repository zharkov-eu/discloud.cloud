package ru.discloud.user.web.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserRequest {
    @Email(message = "Provided email isn't valid")
    private String email;

    @NotEmpty(message = "Phone not provided")
    private String phone;

    private String username;

    @Size(min = 8, message = "Provided password length less than 8 symbols")
    private String password;
}