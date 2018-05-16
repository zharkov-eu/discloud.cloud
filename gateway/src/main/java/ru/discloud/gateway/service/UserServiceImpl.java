package ru.discloud.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.*;
import org.asynchttpclient.util.HttpConstants;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.config.EndpointConfig;
import ru.discloud.gateway.web.model.UserRequest;
import ru.discloud.gateway.domain.User;
import ru.discloud.user.domain.UserPrivileges;

import javax.xml.bind.ValidationException;
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
    public Flux<User> getUsers() {
        return null;
    }

    @Override
    public Mono<User> getUser() {
        return null;
    }

    @Override
    public Mono<User> createUser(UserRequest userRequest) throws Exception {
        if (userRequest.getEmail() == null && userRequest.getPhone() == null) {
            throw new ValidationException("Email or phone must be not empty!");
        }
        User user = new User()
                .setUsername(userRequest.getEmail() != null ? userRequest.getEmail() : userRequest.getPhone())
                .setEmail(userRequest.getEmail())
                .setPhone(userRequest.getPhone())
                .setPassword(userRequest.getPassword())
                .setPrivileges(UserPrivileges.USER)
                .setQuota(1024 * 1024L);

        ru.discloud.auth.web.model.UserRequest authUserRequest = new ru.discloud.auth.web.model.UserRequest()
                .setId(1L)
                .setUsername(user.getUsername())
                .setPassword(user.getPassword());
        Request createUserAuthService = new RequestBuilder(HttpConstants.Methods.POST)
                .setUrl(authService.getBaseUrl() + "/api/auth/user")
                .setBody(mapper.writeValueAsString(authUserRequest))
                .build();
        Future<Response> userAuthService = httpClient.executeRequest(createUserAuthService);

        return null;
    }
}
