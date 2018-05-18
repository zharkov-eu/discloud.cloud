package ru.discloud.gateway.request;

public class ServiceBadCredentialsException extends Exception {
    public ServiceBadCredentialsException(String message) {
        super(message);
    }
}
