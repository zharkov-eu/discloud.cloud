package ru.discloud.gateway.exception.service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.discloud.gateway.request.service.ServiceEnum;

@Data
@EqualsAndHashCode(callSuper = true)
public class EntityExistsException extends RuntimeException {
  private ServiceEnum service;
  private ServiceException exception;

  public EntityExistsException(ServiceEnum service, ServiceException exception) {
    super(String.format("{%s}: %s", service, exception.getMessage()));
    this.service = service;
    this.exception = exception;
  }
}
