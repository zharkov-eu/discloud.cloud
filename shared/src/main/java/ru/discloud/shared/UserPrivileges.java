package ru.discloud.shared;

public enum UserPrivileges {
    ADMIN("admin"),
    USER("user");

    private final String text;

    UserPrivileges(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}