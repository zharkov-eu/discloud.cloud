package ru.discloud.shared.web.core;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Accessors(chain = true)
public class GroupRequest {
  @Positive
  protected Integer id;

  @NotBlank
  protected String name;

  @NotNull
  protected Boolean system;
}
