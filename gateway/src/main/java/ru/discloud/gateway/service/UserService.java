package ru.discloud.gateway.service;

import reactor.core.publisher.Mono;
import ru.discloud.gateway.domain.User;
import ru.discloud.gateway.web.model.UserPageResponse;
import ru.discloud.gateway.web.model.UserRequest;

public interface UserService {
  Mono<UserPageResponse> getUsers();

  Mono<User> getUserById(Long id);

  Mono<User> getUserBy(String username, String email, String phone);

  Mono<User> createUser(UserRequest userRequest) throws Exception;
}
