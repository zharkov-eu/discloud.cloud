package ru.discloud.gateway.exception;

public class EntityExistsException extends RuntimeException {
  public EntityExistsException(String message) {
    super(message);
  }
}
