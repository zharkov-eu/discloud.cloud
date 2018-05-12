package ru.discloud.auth.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.discloud.auth.web.model.ErrorResponse;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse notFound(EntityNotFoundException exception) {
        logger.error("Entity Not Found exception:" + exception.getMessage());
        return new ErrorResponse("EntityNotFound", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityExistsException.class)
    public ErrorResponse exists(EntityExistsException exception) {
        logger.error("Entity Exists exception:" + exception.getMessage());
        return new ErrorResponse("EntityExists", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException.class)
    public ErrorResponse dataAccess(DataAccessException exception) {
        logger.error("DataAccessException exception: " + exception.getMessage());
        return new ErrorResponse("DataAccess", exception.getMessage());
    }
}
