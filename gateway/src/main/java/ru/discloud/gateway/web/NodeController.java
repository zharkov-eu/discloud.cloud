package ru.discloud.gateway.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.service.NodeService;
import ru.discloud.gateway.web.model.NodeResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/node")
public class NodeController {
  private final NodeService nodeService;

  @Autowired
  public NodeController(NodeService nodeService) {
    this.nodeService = nodeService;
  }

  @GetMapping(path = "/")
  public Mono<List<NodeResponse>> getNodes() {
    return nodeService.getNodes().map(nodes -> nodes.stream().map(NodeResponse::new).collect(Collectors.toList()));
  }
}
