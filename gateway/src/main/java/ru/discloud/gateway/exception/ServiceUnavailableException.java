package ru.discloud.gateway.exception;

public class ServiceUnavailableException extends Exception {
  public ServiceUnavailableException(String message) {
    super(message);
  }
}
