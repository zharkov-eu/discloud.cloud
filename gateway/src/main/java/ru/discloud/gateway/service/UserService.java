package ru.discloud.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.domain.User;
import ru.discloud.gateway.web.model.UserRequest;

import javax.xml.bind.ValidationException;

public interface UserService {
    Flux<User> getUsers();

    Mono<User> getUser();

    Mono<User> createUser(UserRequest userRequest) throws Exception;
}
