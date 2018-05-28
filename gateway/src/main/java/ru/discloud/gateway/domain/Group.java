package ru.discloud.gateway.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Group {
  private Integer id;
  private String name;
  private Boolean system;
}
