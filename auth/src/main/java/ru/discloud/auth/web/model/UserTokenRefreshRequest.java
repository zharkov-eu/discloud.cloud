package ru.discloud.auth.web.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserTokenRefreshRequest {
  @NotEmpty(message = "Property 'refreshToken' isn't presented")
  String refreshToken;
}
