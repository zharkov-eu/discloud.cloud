package ru.discloud.shared.web.core;

import lombok.Data;

@Data
public class NodeResponse {
  protected String uid;
  protected String ipv4;
  protected String host;
  protected String protocol;
  protected Integer port;
  protected NodeRoleEnum role;
  protected String zone;
}
