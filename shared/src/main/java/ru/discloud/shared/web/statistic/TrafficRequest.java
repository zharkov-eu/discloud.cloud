package ru.discloud.shared.web.statistic;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@Accessors(chain = true)
public class TrafficRequest {
  @NotEmpty
  private String serviceUuid;

  @NotNull
  @PositiveOrZero
  private Integer income;

  @NotNull
  @PositiveOrZero
  private Integer outcome;
}
