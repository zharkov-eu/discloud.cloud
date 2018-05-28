package ru.discloud.shared.web.core;

import lombok.Data;

@Data
public class GroupResponse {
  protected Integer id;
  protected String name;
  protected Boolean system;
}
