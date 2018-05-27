package ru.discloud.auth.web;

import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.discloud.auth.exception.*;
import ru.discloud.shared.web.ErrorResponse;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionController {
  private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ErrorResponse notFound(EntityNotFoundException exception) {
    logger.error("EntityNotFound exception:" + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(EntityExistsException.class)
  public ErrorResponse exists(EntityExistsException exception) {
    logger.error("EntityExists exception:" + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.CONFLICT);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(SignatureException.class)
  public ErrorResponse signature(SignatureException exception) {
    logger.error("Signature exception: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.UNAUTHORIZED);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(AppCredentialsException.class)
  public ErrorResponse appCredentials(AppCredentialsException exception) {
    logger.error("AppCredentials exception:" + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.UNAUTHORIZED);
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(AppPermissionException.class)
  public ErrorResponse appPermission(AppPermissionException exception) {
    logger.error("AppPermission exception:" + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.FORBIDDEN);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(TokenExpiredException.class)
  public ErrorResponse tokenExpired(TokenExpiredException exception) {
    logger.error("TokenExpired exception:" + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.UNAUTHORIZED);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(TokenInvalidException.class)
  public ErrorResponse tokenInvalid(TokenInvalidException exception) {
    logger.error("TokenInvalid exception:" + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.UNAUTHORIZED);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(UserCredentialsException.class)
  public ErrorResponse userCredentials(UserCredentialsException exception) {
    logger.error("UserCredentials exception:" + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.UNAUTHORIZED);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(DataAccessException.class)
  public ErrorResponse dataAccess(DataAccessException exception) {
    logger.error("DataAccess exception: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
