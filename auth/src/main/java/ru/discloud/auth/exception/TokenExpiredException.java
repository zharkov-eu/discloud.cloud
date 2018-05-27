package ru.discloud.auth.exception;

public class TokenExpiredException extends Exception {
  public TokenExpiredException(String message) {
    super(message);
  }
}
