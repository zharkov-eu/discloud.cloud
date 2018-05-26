package ru.discloud.gateway.exception;

public class ServiceResponseParsingException extends RuntimeException {
  public ServiceResponseParsingException(String message) {
    super(message);
  }
}
