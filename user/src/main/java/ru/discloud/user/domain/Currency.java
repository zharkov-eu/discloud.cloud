package ru.discloud.user.domain;

public enum Currency {
  RUB("rub"),
  USD("usd");

  private final String text;

  Currency(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}