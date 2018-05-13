package ru.discloud.auth.web.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class AppTokenInitialRequest {
    @NotNull(message = "Property 'appId' isn't provided")
    @Positive(message = "Property 'appId' must be a positive value")
    private Integer appId;
    @NotEmpty(message = "Property 'deviceId' isn't provided")
    private String deviceId;
    @NotEmpty(message = "Property 'redirectUrl' isn't provided")
    private String redirectUrl;
    @NotEmpty(message = "Property 'username' isn't provided")
    private String username;
    @NotEmpty(message = "Property 'password' isn't provided")
    private String password;
}
