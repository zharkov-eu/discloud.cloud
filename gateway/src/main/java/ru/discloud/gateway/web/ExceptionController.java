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
import ru.discloud.shared.web.ErrorResponse;

import javax.persistence.EntityNotFoundException;


@Slf4j
@RestControllerAdvice
public class ExceptionController {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ErrorResponse notFound(EntityNotFoundException exception) {
    log.error("EntityExistsException: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
  @ExceptionHandler(ServiceUnavailableException.class)
  public ErrorResponse serviceUnavailable(ServiceUnavailableException exception) {
    log.error("ServiceUnavailableException: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.GATEWAY_TIMEOUT);
  }

  @ExceptionHandler(ServiceResponseException.class)
  public ErrorResponse serviceResponse(ServiceResponseException exception, HttpResponse response) {
    response.setStatus(HttpResponseStatus.valueOf(exception.getCode()));
    log.error("ServiceResponseException: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.resolve(exception.getCode()));
  }
}
