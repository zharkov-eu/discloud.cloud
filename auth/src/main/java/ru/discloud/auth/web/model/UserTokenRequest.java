package ru.discloud.auth.web.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserTokenRequest {
    @NotEmpty(message = "Property 'accessToken' isn't presented")
    String accessToken;
}