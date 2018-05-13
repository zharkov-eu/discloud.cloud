package ru.discloud.auth.web.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AppTokenRequest {
    @NotEmpty(message = "Property 'accessToken' isn't provided")
    String accessToken;
}
