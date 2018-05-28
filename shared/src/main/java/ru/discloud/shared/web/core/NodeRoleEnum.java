package ru.discloud.shared.web.core;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NodeRoleEnum {
  MASTER("master"),
  SLAVE("slave");

  private final String text;

  NodeRoleEnum(final String text) {
    this.text = text;
  }

  @Override
  @JsonValue
  public String toString() {
    return text;
  }
}
