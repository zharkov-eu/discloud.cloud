package ru.discloud.gateway.exception;

import lombok.Getter;

@Getter
public class ServiceResponseException extends RuntimeException {
  private final int code;
  private final String response;

  public ServiceResponseException(String message, int code, String response) {
    super(message);
    this.code = code;
    this.response = response;
  }
}
