package ru.discloud.gateway.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EntryType {
  FILE("f"),
  DIRECTORY("d");

  private final String text;

  EntryType(final String text) {
    this.text = text;
  }

  @Override
  @JsonValue
  public String toString() {
    return text;
  }
}
