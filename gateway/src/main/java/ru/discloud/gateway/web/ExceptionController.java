package ru.discloud.gateway.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.discloud.gateway.exception.EntityExistsException;
import ru.discloud.gateway.exception.EntityNotFoundException;
import ru.discloud.gateway.exception.ServiceResponseException;
import ru.discloud.gateway.exception.ServiceUnavailableException;
import ru.discloud.gateway.request.store.MasterNodeNotExistsException;
import ru.discloud.shared.web.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ErrorResponse notFound(EntityNotFoundException exception) {
    log.error("EntityNotFoundException: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(EntityExistsException.class)
  public ErrorResponse entityExists(EntityExistsException exception) {
    log.error("EntityExistsException: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.CONFLICT);
  }

  @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
  @ExceptionHandler(MasterNodeNotExistsException.class)
  public ErrorResponse masterNodeNotFound(MasterNodeNotExistsException exception) {
    log.error("MasterNodeNotExistsException: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.GATEWAY_TIMEOUT);
  }

  @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
  @ExceptionHandler(ServiceUnavailableException.class)
  public ErrorResponse serviceUnavailable(ServiceUnavailableException exception) {
    log.error("ServiceUnavailableException: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.GATEWAY_TIMEOUT);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(JsonProcessingException.class)
  public ErrorResponse jsonProcessing(JsonProcessingException exception) {
    log.error("JsonProcessingException: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ServiceResponseException.class)
  public ErrorResponse serviceResponse(ServiceResponseException exception, HttpResponse response) {
    response.setStatus(HttpResponseStatus.valueOf(exception.getCode()));
    log.error("ServiceResponseException: " + exception.getMessage());
    return new ErrorResponse(exception, HttpStatus.resolve(exception.getCode()));
  }
}
