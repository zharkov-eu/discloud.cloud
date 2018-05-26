package ru.discloud.gateway.web;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.discloud.gateway.exception.ServiceResponseException;
import ru.discloud.gateway.exception.ServiceUnavailableException;
import ru.discloud.gateway.exception.service.EntityNotFoundException;
import ru.discloud.user.web.model.ErrorResponse;


@Slf4j
@RestControllerAdvice
public class ExceptionController {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ErrorResponse notFound(EntityNotFoundException exception) {
    log.error("EntityExistsException: " + exception.getMessage());
    return new ErrorResponse().setError("EntityExistsException")
        .setMessage(exception.getMessage())
        .setTimestamp();
  }

//  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
//  @ExceptionHandler(ServiceUnavailableException.class)
//  public ErrorResponse serviceUnavailable(ServiceUnavailableException exception) {
//    log.error("ServiceUnavailableException: " + exception.getMessage());
//    return new ErrorResponse("ServiceUnavailableException", exception.getMessage());
//  }
//
//  @ExceptionHandler(ServiceResponseException.class)
//  public ErrorResponse serviceResponse(ServiceResponseException exception, HttpResponse response) {
//    response.setStatus(HttpResponseStatus.valueOf(exception.getCode()));
//    log.error("ServiceResponseException: " + exception.getMessage());
//    return new ErrorResponse("ServiceResponseException", exception.getResponse());
//  }
}
