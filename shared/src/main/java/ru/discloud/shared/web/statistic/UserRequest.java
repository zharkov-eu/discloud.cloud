package ru.discloud.shared.web.statistic;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.discloud.shared.web.UtmLabel;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class UserRequest {
  @NotBlank
  private String username;

  @Nullable
  private UtmLabel utm;
}
