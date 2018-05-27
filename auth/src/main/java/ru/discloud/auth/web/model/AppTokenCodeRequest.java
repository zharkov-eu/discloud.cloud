package ru.discloud.auth.web.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class AppTokenCodeRequest {
  @NotNull(message = "Property 'appId' isn't provided")
  @Positive(message = "Property 'appId' must be a positive value")
  private Integer appId;
  @NotEmpty(message = "Property 'appSecret' isn't provided")
  private String appSecret;
  @NotEmpty(message = "Property 'code' isn't provided")
  private String code;
}
