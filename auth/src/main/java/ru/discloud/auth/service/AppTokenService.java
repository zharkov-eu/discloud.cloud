package ru.discloud.auth.service;

import ru.discloud.auth.domain.AppToken;
import ru.discloud.auth.domain.User;
import ru.discloud.auth.exception.AppCredentialsException;
import ru.discloud.auth.exception.TokenExpiredException;
import ru.discloud.auth.exception.TokenInvalidException;
import ru.discloud.auth.exception.UserCredentialsException;
import ru.discloud.auth.web.model.AppTokenCodeRequest;
import ru.discloud.auth.web.model.AppTokenInitialRequest;
import ru.discloud.auth.web.model.AppTokenRefreshRequest;
import ru.discloud.auth.web.model.AppTokenRequest;

public interface AppTokenService {
    AppToken create(AppTokenInitialRequest appTokenInitialRequest) throws UserCredentialsException;

    AppToken findByAppCode(AppTokenCodeRequest appTokenCodeRequest) throws AppCredentialsException;

    AppToken refresh(AppTokenRefreshRequest appTokenRefreshRequest) throws Exception;

    User checkAccessToken(AppTokenRequest appTokenRequest) throws TokenExpiredException, TokenInvalidException;

    void deleteToken(AppTokenRequest appTokenRequest) throws TokenInvalidException;
}
