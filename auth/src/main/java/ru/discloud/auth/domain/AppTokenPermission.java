package ru.discloud.auth.domain;

public enum AppTokenPermission {
    READONLY("readonly"),
    READWRITE("readwrite");

    private final String permission;

    AppTokenPermission(final String text) {
        this.permission = text;
    }

    @Override
    public String toString() { return permission; }
}
