package ru.discloud.auth.web.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class UserRequest {
    @NotNull(message = "Property 'id' isn't presented")
    @Positive(message = "Property 'id' must be positive value")
    Integer id;

    @NotEmpty(message = "Property 'username' isn't presented")
    String username;

    @NotEmpty(message = "Property 'password' isn't presented")
    @Length(min = 8, message = "Property 'password' must be more than 8 symbols length")
    String password;
}
