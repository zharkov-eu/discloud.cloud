package ru.discloud.statistics.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.discloud.shared.web.UtmLabel;

@Data
@Accessors(chain = true)
public class User {
  private String username;

  @Nullable
  private UtmLabel utmLabel;
}
