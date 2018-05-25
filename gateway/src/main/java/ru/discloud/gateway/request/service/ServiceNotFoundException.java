package ru.discloud.gateway.request.service;

class ServiceNotFoundException extends RuntimeException {
  ServiceNotFoundException(String message) {
    super(message);
  }
}
