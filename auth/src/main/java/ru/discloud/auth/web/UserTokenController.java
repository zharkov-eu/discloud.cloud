package ru.discloud.auth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.discloud.auth.domain.User;
import ru.discloud.auth.domain.UserToken;
import ru.discloud.auth.exception.TokenExpiredException;
import ru.discloud.auth.exception.TokenInvalidException;
import ru.discloud.auth.exception.UserCredentialsException;
import ru.discloud.auth.service.UserTokenService;
import ru.discloud.auth.web.model.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth/token")
public class UserTokenController {

  private final UserTokenService userTokenService;

  @Autowired
  public UserTokenController(UserTokenService userTokenService) {
    this.userTokenService = userTokenService;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(method = RequestMethod.POST)
  public UserTokenResponse createToken(@Valid @RequestBody UserTokenInitialRequest tokenRequest) throws UserCredentialsException {
    UserToken userToken = this.userTokenService.create(tokenRequest);
    return new UserTokenResponse(userToken);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(value = "/refresh", method = RequestMethod.POST)
  public UserTokenResponse refreshToken(@Valid @RequestBody UserTokenRefreshRequest refreshTokenRequest)
      throws TokenExpiredException, TokenInvalidException {
    UserToken userToken = this.userTokenService.refresh(refreshTokenRequest);
    return new UserTokenResponse(userToken);
  }

  @RequestMapping(value = "/check", method = RequestMethod.POST)
  public UserResponse checkToken(@Valid @RequestBody UserTokenRequest userTokenRequest)
      throws TokenExpiredException, TokenInvalidException {
    User user = this.userTokenService.checkAccessToken(userTokenRequest);
    return new UserResponse(user);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RequestMapping(method = RequestMethod.DELETE)
  public void deleteToken(@Valid @RequestBody UserTokenRemoveRequest userTokenRequest) {
    userTokenService.deleteToken(userTokenRequest);
  }
}