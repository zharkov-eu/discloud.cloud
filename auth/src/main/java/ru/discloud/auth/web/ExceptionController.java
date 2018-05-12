package ru.discloud.auth.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.discloud.auth.exception.*;
import ru.discloud.auth.web.model.ErrorResponse;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse notFound(EntityNotFoundException exception) {
        logger.error("EntityNotFound exception:" + exception.getMessage());
        return new ErrorResponse("EntityNotFoundException", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityExistsException.class)
    public ErrorResponse exists(EntityExistsException exception) {
        logger.error("EntityExists exception:" + exception.getMessage());
        return new ErrorResponse("EntityExistsException", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException.class)
    public ErrorResponse dataAccess(DataAccessException exception) {
        logger.error("DataAccessException exception: " + exception.getMessage());
        return new ErrorResponse("DataAccessException", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AppCredentialsException.class)
    public ErrorResponse appCredentials(AppCredentialsException exception) {
        logger.error("AppCredentials exception:" + exception.getMessage());
        return new ErrorResponse("AppCredentialsException", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AppPermissionException.class)
    public ErrorResponse appPermission(AppPermissionException exception) {
        logger.error("AppPermission exception:" + exception.getMessage());
        return new ErrorResponse("AppPermissionException", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenExpiredException.class)
    public ErrorResponse tokenExpired(TokenExpiredException exception) {
        logger.error("TokenExpired exception:" + exception.getMessage());
        return new ErrorResponse("TokenExpiredException", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenInvalidException.class)
    public ErrorResponse tokenInvalid(TokenInvalidException exception) {
        logger.error("TokenInvalid exception:" + exception.getMessage());
        return new ErrorResponse("TokenInvalidException", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserCredentialsException.class)
    public ErrorResponse userCredentials(UserCredentialsException exception) {
        logger.error("UserCredentials exception:" + exception.getMessage());
        return new ErrorResponse("UserCredentialsException", exception.getMessage());
    }
}
