package ru.discloud.user.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.discloud.shared.auth.ProxyBadCredentialsException;
import ru.discloud.shared.auth.ProxyTokenInvalidException;
import ru.discloud.shared.web.ErrorResponse;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionController {
  private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ErrorResponse notFound(EntityNotFoundException exception) {
    logger.error("EntityNotFoundException: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.PROXY_AUTHENTICATION_REQUIRED)
  @ExceptionHandler(ProxyBadCredentialsException.class)
  public ErrorResponse proxyAuth(ProxyBadCredentialsException exception) {
    logger.error("ProxyBadCredentialsException: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
  }

  @ResponseStatus(HttpStatus.PROXY_AUTHENTICATION_REQUIRED)
  @ExceptionHandler(ProxyTokenInvalidException.class)
  public ErrorResponse tokenInvalid(ProxyTokenInvalidException exception) {
    logger.error("ProxyTokenInvalidException: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(DataAccessException.class)
  public ErrorResponse dataAccess(DataAccessException exception) {
    logger.error("DataAccessException: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
