package ru.discloud.gateway.request.store;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class FileNode {
  private String uid;
  private String ipv4;
  private String host;
  private String protocol;
  private Integer port;
  private FileNodeRoleEnum role;
  private String zone;

  String getBaseUrl() {
    return protocol + "://" + (host != null ? host : ipv4) + ":" + port;
  }
}
