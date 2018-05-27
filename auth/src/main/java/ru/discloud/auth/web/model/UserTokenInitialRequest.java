package ru.discloud.auth.web.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserTokenInitialRequest {
  @NotEmpty(message = "Property 'deviceId' isn't presented")
  String deviceId;

  @NotEmpty(message = "Property 'username' isn't presented")
  String username;

  @NotEmpty(message = "Property 'password' isn't presented")
  String password;
}
