package ru.discloud.gateway.request;

class ServiceNotFoundException extends RuntimeException {
    ServiceNotFoundException(String message) {
        super(message);
    }
}
