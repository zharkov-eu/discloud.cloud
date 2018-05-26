package ru.discloud.gateway.exception;

import lombok.Getter;
import org.asynchttpclient.Response;
import ru.discloud.gateway.request.service.ServiceEnum;

@Getter
public class ServiceResponseException extends RuntimeException {
  private final int code;
  private final String response;

  public ServiceResponseException(ServiceEnum service, Response response) {
    super(String.format("Service %s bad response: code = %s, response = %s", service, response.getStatusCode(), response.getResponseBody()));
    this.code = response.getStatusCode();
    this.response = response.getResponseBody();
  }
}
