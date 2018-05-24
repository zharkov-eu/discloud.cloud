package ru.discloud.gateway.request;

import lombok.AllArgsConstructor;
import org.asynchttpclient.*;
import org.springframework.stereotype.Component;
import ru.discloud.gateway.config.EndpointConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
public class AuthServiceRequest {
    private static final Integer DEFAULT_TIMEOUT = 60000;

    private final EndpointConfig authService = new EndpointConfig("auth");
    private final EndpointConfig statisticsService = new EndpointConfig("statistics");
    private final EndpointConfig userService = new EndpointConfig("user");
    private final AsyncHttpClient httpClient;

    private Map<ServiceEnum, Token> serviceTokens;

    public AuthServiceRequest() throws IOException {
        DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
                .setConnectTimeout(DEFAULT_TIMEOUT);
        this.httpClient = Dsl.asyncHttpClient(clientBuilder);
        this.serviceTokens = new HashMap<>();
    }

    public CompletableFuture<Response> request(ServiceEnum service, String method, String path) {
        RequestBuilder requestBuilder = new RequestBuilder(method);
        return executeRequest(service, requestBuilder, path);
    }

    public CompletableFuture<Response> request(ServiceEnum service, String method, String path,
                                               Map<String, List<String>> queryParams) {
        RequestBuilder requestBuilder = new RequestBuilder(method);
        if (queryParams != null) requestBuilder.setQueryParams(queryParams);
        return executeRequest(service, requestBuilder, path);
    }

    public CompletableFuture<Response> request(ServiceEnum service, String method, String path,
                                               Map<String, List<String>> queryParams, String body) {
        RequestBuilder requestBuilder = new RequestBuilder(method);
        if (queryParams != null) requestBuilder.setQueryParams(queryParams);
        if (body != null) requestBuilder.setBody(body);
        return executeRequest(service, requestBuilder, path);
    }

    private CompletableFuture<Response> executeRequest(ServiceEnum service,
                                                       RequestBuilder requestBuilder, String path) {
        String url = getUrl(service, path);

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