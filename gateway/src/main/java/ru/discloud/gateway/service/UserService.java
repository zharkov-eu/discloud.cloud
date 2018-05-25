package ru.discloud.gateway.service;

import reactor.core.publisher.Mono;
import ru.discloud.gateway.domain.User;
import ru.discloud.gateway.web.model.UserPageResponse;
import ru.discloud.gateway.web.model.UserRequest;

public interface UserService {
  Mono<UserPageResponse> getUsers() throws Exception;

  Mono<User> getUserById(Long id);

  Mono<User> getUserByUsername(String username);

  Mono<User> createUser(UserRequest userRequest) throws Exception;
}
