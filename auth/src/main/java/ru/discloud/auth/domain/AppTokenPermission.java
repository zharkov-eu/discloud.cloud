package ru.discloud.auth.domain;

public enum AppTokenPermission {
    READONLY("readonly"),
    READWRITE("readwrite");

    private final String text;

    AppTokenPermission(final String text) {
        this.text = text;
    }

    @Override
    public String toString() { return text; }
}
