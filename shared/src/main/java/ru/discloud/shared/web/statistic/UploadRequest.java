package ru.discloud.shared.web.statistic;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Accessors(chain = true)
public class UploadRequest {
  @NotBlank
  private String username;

  @NonNull
  private Boolean encrypted;

  @NotNull
  @Positive
  private Integer size;
}
