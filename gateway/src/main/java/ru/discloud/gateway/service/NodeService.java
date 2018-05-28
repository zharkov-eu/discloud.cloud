package ru.discloud.gateway.service;

import reactor.core.publisher.Mono;
import ru.discloud.gateway.domain.Node;

import java.util.List;

public interface NodeService {
  Mono<List<Node>> getNodes();
}
