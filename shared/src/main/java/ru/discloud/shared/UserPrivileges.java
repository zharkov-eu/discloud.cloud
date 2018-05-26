package ru.discloud.shared;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserPrivileges {
  ADMIN("admin"),
  USER("user");

  private final String text;

  UserPrivileges(final String text) {
    this.text = text;
  }

  @Override
  @JsonValue
  public String toString() {
    return text;
  }
}