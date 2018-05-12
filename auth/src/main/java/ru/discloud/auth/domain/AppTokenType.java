package ru.discloud.auth.domain;

public enum AppTokenType {
    ETERNAL("eternal"),
    EXTEND("extend");

    private final String text;

    AppTokenType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
