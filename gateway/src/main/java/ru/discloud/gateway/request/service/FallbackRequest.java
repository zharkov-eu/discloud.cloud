package ru.discloud.gateway.request.service;

import lombok.AllArgsConstructor;
import org.asynchttpclient.Response;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class FallbackRequest {
  public ServiceEnum service;
  public Mono<Response> request;
}
