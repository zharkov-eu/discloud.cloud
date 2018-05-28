package ru.discloud.gateway.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.discloud.shared.web.core.NodeRoleEnum;

@Data
@Accessors(chain = true)
public class Node {
  private String uid;
  private String ipv4;
  private String host;
  private String protocol;
  private Integer port;
  private NodeRoleEnum role;
  private String zone;

  public String getBaseUrl() {
    return protocol + "://" + (host != null ? host : ipv4) + ":" + port;
  }
}
