package ru.discloud.gateway.request;

public class ServiceNotFoundException extends Exception {
    public ServiceNotFoundException(String message) {
        super(message);
    }
}
