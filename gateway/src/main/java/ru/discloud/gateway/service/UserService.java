package ru.discloud.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.discloud.gateway.web.model.UserRequest;
import ru.discloud.gateway.web.model.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest userRequest) throws JsonProcessingException;
}
