package ru.discloud.auth.service;

import ru.discloud.auth.domain.User;
import ru.discloud.auth.domain.UserToken;
import ru.discloud.auth.exception.TokenExpiredException;
import ru.discloud.auth.exception.TokenInvalidException;
import ru.discloud.auth.exception.UserCredentialsException;
import ru.discloud.auth.web.model.UserTokenInitialRequest;
import ru.discloud.auth.web.model.UserTokenRefreshRequest;
import ru.discloud.auth.web.model.UserTokenRemoveRequest;
import ru.discloud.auth.web.model.UserTokenRequest;

public interface UserTokenService {
    UserToken create(UserTokenInitialRequest userTokenInitialRequest) throws UserCredentialsException;

    UserToken refresh(UserTokenRefreshRequest userTokenRefreshRequest) throws TokenExpiredException, TokenInvalidException;

    User checkAccessToken(UserTokenRequest userTokenRequest) throws TokenExpiredException, TokenInvalidException;

    void deleteToken(UserTokenRemoveRequest userTokenRequest);
}
