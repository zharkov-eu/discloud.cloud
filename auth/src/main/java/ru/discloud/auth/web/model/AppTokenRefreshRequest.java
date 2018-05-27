package ru.discloud.auth.web.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AppTokenRefreshRequest {
  @NotEmpty(message = "Property 'refreshToken' isn't provided")
  private String refreshToken;
}
