package ru.discloud.gateway.request.service;

import lombok.AllArgsConstructor;
import org.asynchttpclient.*;
import org.springframework.stereotype.Component;
import ru.discloud.gateway.config.EndpointConfig;
import ru.discloud.gateway.exception.ServiceUnavailableException;

import java.io.IOException;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Component
public class AuthRequestService {
  private static final Integer DEFAULT_TIMEOUT = 1000;

  private final EndpointConfig authService = new EndpointConfig("auth");
  private final EndpointConfig fileService = new EndpointConfig("file");
  private final EndpointConfig statisticsService = new EndpointConfig("statistics");
  private final EndpointConfig userService = new EndpointConfig("user");
  private final AsyncHttpClient httpClient;

  private Map<ServiceEnum, Token> serviceTokens;

  public AuthRequestService() throws IOException {
    DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
        .setConnectTimeout(DEFAULT_TIMEOUT);
    this.httpClient = Dsl.asyncHttpClient(clientBuilder);
    this.serviceTokens = new HashMap<>();
  }

  public CompletableFuture<Response> request(ServiceEnum service, String method, String url) {
    RequestBuilder requestBuilder = new RequestBuilder(method);
    return executeRequest(service, requestBuilder, url);
  }

  public CompletableFuture<Response> request(ServiceEnum service, String method, String url,
                                             Map<String, List<String>> queryParams) {
    RequestBuilder requestBuilder = new RequestBuilder(method);
    if (queryParams != null) requestBuilder.setQueryParams(queryParams);
    return executeRequest(service, requestBuilder, url);
  }

  public CompletableFuture<Response> request(ServiceEnum service, String method, String url,
                                             Map<String, List<String>> queryParams, String body) {
    RequestBuilder requestBuilder = new RequestBuilder(method);
    if (queryParams != null) requestBuilder.setQueryParams(queryParams);
    if (body != null) requestBuilder.setBody(body);
    return executeRequest(service, requestBuilder, url);
  }

  private CompletableFuture<Response> executeRequest(ServiceEnum service,
                                                     RequestBuilder requestBuilder, String url) {
    url = url.startsWith("http") ? url : getUrl(service, url);

    String authorizationHeader;
    Token token = serviceTokens.get(service);
    if (token != null && new Date().before(token.expired)) {
      authorizationHeader = "Bearer " + token.value;
    } else {
      authorizationHeader = "Basic " + getBasicAuthCredentials(service);
    }

    requestBuilder.setUrl(url)
        .setHeader("Authorization", authorizationHeader)
        .setHeader("Content-Type", "application/json");

    return httpClient.executeRequest(requestBuilder).toCompletableFuture()
        .thenCompose((response) -> {
          if (response.getStatusCode() == 407) {
            requestBuilder.setHeader("Authorization", "Basic " + getBasicAuthCredentials(service));
            return httpClient.executeRequest(requestBuilder).toCompletableFuture()
                .thenApply(authResponse -> {
                  if (authResponse.getStatusCode() == 407) {
                    throw new ServiceCredentialsException(
                        String.format("Service %s not found", service.toString()));
                  }
                  String tokenString = authResponse.getHeader("X-Auth-Token");
                  long tokenExpireInterval = Long.parseLong(authResponse.getHeader("X-Auth-Token"));
                  Date tokenExpire = new Date(new Date().getTime() + tokenExpireInterval);
                  serviceTokens.put(service, new Token(tokenString, tokenExpire));

                  return authResponse;
                });
          } else {
            return CompletableFuture.supplyAsync(() -> response);
          }
        }).exceptionally((ex) -> {
          if (ex instanceof CompletionException && ex.getCause() instanceof ConnectException) {
            throw new ServiceUnavailableException(String.format("{'%s'} service unavailable", service.toString()));
          }
          else throw new RuntimeException(ex);
        });
  }

  private String getUrl(ServiceEnum service, String path) throws ServiceNotFoundException {
    switch (service) {
      case AUTH:
        return authService.getBaseUrl().concat(path);
      case STATISTICS:
        return statisticsService.getBaseUrl().concat(path);
      case USER:
        return userService.getBaseUrl().concat(path);
      default:
        throw new ServiceNotFoundException(String.format("Service %s not found", service.toString()));
    }
  }

  private String getBasicAuthCredentials(ServiceEnum service) throws ServiceNotFoundException {
    String credentials;
    switch (service) {
      case AUTH:
        credentials = authService.getUser() + ":" + authService.getPassword();
        break;
      case FILE:
        credentials = fileService.getUser() + ":" + fileService.getPassword();
      case STATISTICS:
        credentials = statisticsService.getUser() + ":" + statisticsService.getPassword();
        break;
      case USER:
        credentials = userService.getUser() + ":" + userService.getPassword();
        break;
      default:
        throw new ServiceNotFoundException(String.format("Service %s not found", service.toString()));
    }
    return Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
  }
}

@AllArgsConstructor
class Token {
  public String value;
  public Date expired;
}