package ru.discloud.gateway.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EntryShare {
  NONE("n"),
  LINK("l");

  private final String text;

  EntryShare(final String text) {
    this.text = text;
  }

  @Override
  @JsonValue
  public String toString() {
    return text;
  }
}
