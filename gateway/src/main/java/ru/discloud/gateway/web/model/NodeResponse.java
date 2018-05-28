package ru.discloud.gateway.web.model;

import ru.discloud.gateway.domain.Node;

public class NodeResponse extends ru.discloud.shared.web.core.NodeResponse {
  public NodeResponse(Node node) {
    this.uid = node.getUid();
    this.ipv4 = node.getIpv4();
    this.host = node.getHost();
    this.protocol = node.getProtocol();
    this.port = node.getPort();
    this.role = node.getRole();
    this.zone = node.getZone();
  }
}
