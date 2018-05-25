package ru.discloud.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.domain.User;
import ru.discloud.gateway.exception.ServiceResponseException;
import ru.discloud.gateway.request.service.AuthRequestService;
import ru.discloud.gateway.request.service.ServiceEnum;
import ru.discloud.gateway.web.model.UserPageResponse;
import ru.discloud.gateway.web.model.UserRequest;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
  private final ObjectMapper mapper = new ObjectMapper();
  private AuthRequestService authRequest;

  @Autowired
  public UserServiceImpl(AuthRequestService authRequest) throws IOException {
    this.authRequest = authRequest;
  }

  @Override
  public Mono<UserPageResponse> getUsers() {
    return Mono.fromFuture(
        authRequest.request(ServiceEnum.USER, "GET", "/api/user/user/")
    ).map(response -> {
      try {
        return mapper.readValue(response.getResponseBody(), UserPageResponse.class);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Override
  public Mono<User> getUserById(Long id) {
    return Mono.fromFuture(
        authRequest.request(ServiceEnum.USER, "GET", "/api/user/user/" + id)
    ).map(this::mapResponseToUser);
  }

  @Override
  public Mono<User> getUserBy(String username, String email, String phone) {
    Map<String, List<String>> query = new HashMap<>();
    if (username != null) query.put("username", Collections.singletonList(username));
    if (email != null) query.put("email", Collections.singletonList(email));
    if (phone != null) query.put("phone", Collections.singletonList(phone));

    return Mono.fromFuture(
        authRequest.request(ServiceEnum.USER, "GET", "/api/user/user/by/", query)
    ).map(this::mapResponseToUser);
  }

  @Override
  public Mono<User> createUser(UserRequest userRequest) throws Exception {
    if (userRequest.getEmail() == null && userRequest.getPhone() == null) {
      throw new ValidationException("Email or phone must be not empty!");
    }
//        User user = new User()
//                .setUsername(userRequest.getEmail() != null ? userRequest.getEmail() : userRequest.getPhone())
//                .setEmail(userRequest.getEmail())
//                .setPhone(userRequest.getPhone())
//                .setPassword(userRequest.getPassword())
//                .setPrivileges(UserPrivileges.USER)
//                .setQuota(1024 * 1024L);
//
//        ru.discloud.shared.web.user.UserRequest userUserRequest = new ru.discloud.user.web.model.UserRequest()
//                .setUsername(user.getUsername())
//                .setEmail(user.getEmail())
//                .setPhone(user.getPhone())
//                .setUserPrivileges(user.getPrivileges().toString())
//                .setQuota(user.getQuota());
//        Request createUserUserService = new RequestBuilder(HttpConstants.Methods.POST)
//                .setUrl(authService.getBaseUrl() + "/api/user/user")
//                .setBody(mapper.writeValueAsString(userUserRequest))
//                .build();
//        Future<Response> userUserService = httpClient.executeRequest(createUserUserService);
//        if (userUserService.get().getStatusCode() == HttpStatus.CREATED.value()) {
//            ru.discloud.user.web.model.UserResponse userUserResponse = mapper.readValue(
//                    userUserService.get().getResponseBody(), ru.discloud.user.web.model.UserResponse.class);
//            user.setId(userUserResponse.getId());
//        }
//
//        ru.discloud.auth.web.model.UserRequest authUserRequest = new ru.discloud.auth.web.model.UserRequest()
//                .setId(user.getId())
//                .setUsername(user.getUsername())
//                .setPassword(user.getPassword());
//        Request createUserAuthService = new RequestBuilder(HttpConstants.Methods.POST)
//                .setUrl(authService.getBaseUrl() + "/api/auth/user")
//                .setBody(mapper.writeValueAsString(authUserRequest))
//                .build();
//        Future<Response> userAuthService = httpClient.executeRequest(createUserAuthService);

    return null;
  }

  private User mapResponseToUser(Response response) {
    switch (response.getStatusCode()) {
      case 200:
        try {
          return mapper.readValue(response.getResponseBody(), User.class);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      case 404:
        throw new EntityNotFoundException("User not found");
      default:
        throw new ServiceResponseException(
            String.format("Service %s bad response: code = %s, response = %s", ServiceEnum.USER, response.getStatusCode(), response.getResponseBody()),
            response.getStatusCode(),
            response.getResponseBody()
        );
    }
  }
}
