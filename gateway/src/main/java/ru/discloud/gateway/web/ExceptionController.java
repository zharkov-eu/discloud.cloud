package ru.discloud.gateway.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.discloud.user.web.model.ErrorResponse;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse notFound(EntityNotFoundException exception) {
        log.error("EntityNotFound:" + exception.getMessage());
        return new ErrorResponse("EntityNotFound", exception.getMessage());
    }
}
