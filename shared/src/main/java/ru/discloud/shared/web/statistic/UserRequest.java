package ru.discloud.shared.web.statistic;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.discloud.shared.web.UtmLabel;

@Data
@Accessors(chain = true)
public class UserRequest {
  private String username;
  private UtmLabel utm;
}
