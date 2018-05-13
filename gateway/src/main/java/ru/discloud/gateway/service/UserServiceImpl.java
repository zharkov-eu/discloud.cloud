package ru.discloud.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.*;
import org.asynchttpclient.util.HttpConstants;
import org.springframework.stereotype.Service;
import ru.discloud.gateway.config.EndpointConfig;
import ru.discloud.gateway.web.model.UserRequest;
import ru.discloud.gateway.web.model.UserResponse;

import java.io.IOException;
import java.util.concurrent.Future;

@Service
public class UserServiceImpl implements UserService {
    private final EndpointConfig authService = new EndpointConfig("auth");
    private final EndpointConfig statisticsService = new EndpointConfig("statistics");
    private final EndpointConfig userService = new EndpointConfig("user");
    private final ObjectMapper mapper = new ObjectMapper();
    private AsyncHttpClient httpClient;

    public UserServiceImpl() throws IOException {
        DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
                .setConnectTimeout(1000);
        this.httpClient = Dsl.asyncHttpClient(clientBuilder);
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) throws JsonProcessingException {
        Request createUserAuthService = new RequestBuilder(HttpConstants.Methods.POST)
                .setUrl(authService.getBaseUrl() + "/api/auth/user")
                .setBody(mapper.writeValueAsString(userRequest))
                .build();
        Future<Response> userAuthService = httpClient.executeRequest(createUserAuthService);
        return null;
    }
}
