package ru.discloud.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.domain.User;
import ru.discloud.gateway.exception.ServiceResponseParsingException;
import ru.discloud.gateway.repository.redis.UserStatisticQueue;
import ru.discloud.gateway.request.service.AuthRequestService;
import ru.discloud.gateway.request.service.FallbackRequest;
import ru.discloud.gateway.request.service.ServiceEnum;
import ru.discloud.gateway.request.store.FileStoreRequestService;
import ru.discloud.gateway.web.model.UserPageResponse;
import ru.discloud.gateway.web.model.UserRequest;
import ru.discloud.shared.UserPrivileges;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
  private final ObjectMapper mapper = new ObjectMapper();
  private final AuthRequestService authRequest;
  private final FileStoreRequestService fileStore;
  private final UserStatisticQueue userStatisticQueue;

  @Autowired
  public UserServiceImpl(AuthRequestService authRequest,
                         FileStoreRequestService fileRequest,
                         UserStatisticQueue userStatisticQueue) {
    this.authRequest = authRequest;
    this.fileStore = fileRequest;
    this.userStatisticQueue = userStatisticQueue;
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
    return Mono
        .fromFuture(authRequest.request(ServiceEnum.USER, "GET", "/api/user/user/" + id))
        .doOnSuccess(response -> AuthRequestService.checkServiceResponse(ServiceEnum.USER, response))
        .map(this::mapResponseToUser);
  }

  @Override
  public Mono<User> getUserBy(String username, String email, String phone) {
    Map<String, List<String>> query = new HashMap<>();
    if (username != null) query.put("username", Collections.singletonList(username));
    if (email != null) query.put("email", Collections.singletonList(email));
    if (phone != null) query.put("phone", Collections.singletonList(phone));

    return Mono
        .fromFuture(authRequest.request(ServiceEnum.USER, "GET", "/api/user/user/by/", query))
        .doOnSuccess(response -> AuthRequestService.checkServiceResponse(ServiceEnum.USER, response))
        .map(this::mapResponseToUser);
  }

  @Override
  public Mono<User> createUser(UserRequest userRequest) throws ValidationException, JsonProcessingException {
    if (userRequest.getEmail() == null && userRequest.getPhone() == null) {
      throw new ValidationException("Email or phone must be not empty!");
    }

    List<FallbackRequest> fallbackRequest = new ArrayList<>();

    User user = new User()
        .setUsername(userRequest.getEmail() != null ? userRequest.getEmail() : userRequest.getPhone())
        .setEmail(userRequest.getEmail())
        .setPhone(userRequest.getPhone())
        .setGroup(userRequest.getGroup())
        .setPassword(userRequest.getPassword())
        .setPrivileges(UserPrivileges.USER)
        .setQuota(1024 * 1024L);

    ru.discloud.shared.web.core.UserRequest coreUserRequest = new ru.discloud.shared.web.core.UserRequest()
        .setUsername(user.getUsername())
        .setGroup(user.getGroup())
        .setPassword(userRequest.getPassword());

    fileStore.request("POST", "/user", null, mapper.writeValueAsString(coreUserRequest))
        .doOnSuccess(response -> AuthRequestService.checkServiceResponse(ServiceEnum.FILE, response))
        .map(this::mapResponseToUser)
        .doOnSuccess(it -> {
          user.setId(it.getId());
          fallbackRequest.add(
              new FallbackRequest(ServiceEnum.FILE, fileStore.request("DELETE", "/user/" + it.getId()))
          );
        })
        .block();

    ru.discloud.shared.web.user.UserRequest userUserRequest = new ru.discloud.shared.web.user.UserRequest()
        .setId(user.getId())
        .setUsername(user.getUsername())
        .setEmail(user.getEmail())
        .setPhone(user.getPhone())
        .setUserPrivileges(user.getPrivileges().toString())
        .setQuota(user.getQuota());

    Mono<Response> userUserResponse = Mono.fromFuture(
        authRequest.request(ServiceEnum.USER, "POST", "/api/user/user", null, mapper.writeValueAsString(userUserRequest))
    )
        .doOnSuccess(response -> AuthRequestService.checkServiceResponse(ServiceEnum.USER, response))
        .doOnSuccess(response -> fallbackRequest.add(
            new FallbackRequest(
                ServiceEnum.USER,
                Mono.fromFuture(authRequest.request(ServiceEnum.USER, "DELETE", "/api/user/user/" + user.getId())))
            )
        );

    ru.discloud.shared.web.auth.UserRequest authUserRequest = new ru.discloud.shared.web.auth.UserRequest()
        .setId(user.getId())
        .setUsername(user.getUsername())
        .setPassword(user.getPassword());

    Mono<Response> userAuthResponse = Mono.fromFuture(
        authRequest.request(ServiceEnum.AUTH, "POST", "/api/auth/user", null, mapper.writeValueAsString(authUserRequest))
    )
        .doOnSuccess(response -> AuthRequestService.checkServiceResponse(ServiceEnum.AUTH, response))
        .doOnSuccess(response -> fallbackRequest.add(
            new FallbackRequest(
                ServiceEnum.AUTH,
                Mono.fromFuture(authRequest.request(ServiceEnum.AUTH, "DELETE", "/api/auth/user/" + user.getId())))
            )
        );

    ru.discloud.shared.web.statistic.UserRequest statisticsUserRequest = new ru.discloud.shared.web.statistic.UserRequest()
        .setUsername(userRequest.getUsername())
        .setUtm(userRequest.getUtm());

    return Flux.concat(userUserResponse, userAuthResponse)
        .then(Mono.just(user))
        .doOnSuccess(it -> userStatisticQueue.enqueue(statisticsUserRequest));
  }

  @Override
  public Mono<Void> deleteUser(Long id) {
    return Mono
        .fromFuture(authRequest.request(ServiceEnum.USER, "DELETE", "/api/user/user/" + id))
        .doOnSuccess(response -> AuthRequestService.checkServiceResponse(ServiceEnum.USER, response))
        .then();
  }

  private User mapResponseToUser(Response response) {
    try {
      return mapper.readValue(response.getResponseBody(), User.class);
    } catch (IOException ex) {
      throw new ServiceResponseParsingException(ex.getMessage());
    }
  }
}