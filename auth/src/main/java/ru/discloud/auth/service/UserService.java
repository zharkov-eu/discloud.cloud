package ru.discloud.auth.service;

import ru.discloud.auth.domain.User;
import ru.discloud.auth.exception.UserCredentialsException;
import ru.discloud.auth.web.model.UserRequest;

public interface UserService {

    User findById(Long id);

    User findByUsername(String username);

    User checkCredentials(String username, String password) throws UserCredentialsException;

    User save(UserRequest userRequest) throws Exception;

    void delete(Long id);
}
