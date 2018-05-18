package ru.discloud.gateway.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.*;
import org.springframework.stereotype.Component;
import ru.discloud.gateway.config.EndpointConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class AuthServiceRequest {
    private static final Integer DEFAULT_TIMEOUT = 60000;

    private final EndpointConfig authService = new EndpointConfig("auth");
    private final EndpointConfig statisticsService = new EndpointConfig("statistics");
    private final EndpointConfig userService = new EndpointConfig("user");
    private final ObjectMapper mapper = new ObjectMapper();
    private final AsyncHttpClient httpClient;

    private Map<ServiceEnum, String> serviceTokens;

    public AuthServiceRequest() throws IOException {
        DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
                .setConnectTimeout(DEFAULT_TIMEOUT);
        this.httpClient = Dsl.asyncHttpClient(clientBuilder);
        this.serviceTokens = new HashMap<>();
    }

    public CompletableFuture<Response> request(ServiceEnum service, String method, String path,
                                               Map<String, List<String>> queryParams, String body) throws Exception {
        String url = getUrl(service, path);
        String basicAuthCredentials = getBasicAuthCredentials(service);
        RequestBuilder requestBuilder = new RequestBuilder(method)
                .setUrl(url)
                .setHeader("Authorization", "Bearer " + serviceTokens.get(service))
                .setHeader("Content-Type", "application/json");
        if (queryParams != null) requestBuilder.setQueryParams(queryParams);
        if (body != null) requestBuilder.setBody(body);
        Request request = requestBuilder.build();
        return httpClient.executeRequest(requestBuilder).toCompletableFuture()
                .thenApply(response -> {
                    if (response.getStatusCode() == 401) {
                        requestBuilder.setHeader("Authorization", basicAuthCredentials);
                        return httpClient.executeRequest(requestBuilder).toCompletableFuture()
                                .thenApply(authResponse -> {
                                    serviceTokens.put(service, authResponse.getHeader("Auth-Token"));
                                    return authResponse;
                                }).join();
                    } else {
                        return response;
                    }
                }).handle((response, ex) -> {
                    ex.printStackTrace();
                    return response;
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
                throw new ServiceNotFoundException("Service not found");
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
                throw new ServiceNotFoundException("Service not found");
        }
        return Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }
}
