package ru.discloud.gateway.request.store;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FileNodeRoleEnum {
  MASTER("m"),
  SLAVE("s");

  private final String text;

  FileNodeRoleEnum(final String text) {
    this.text = text;
  }

  @Override
  @JsonValue
  public String toString() {
    return text;
  }
}
