package ru.discloud.auth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.discloud.auth.domain.AppToken;
import ru.discloud.auth.exception.AppCredentialsException;
import ru.discloud.auth.exception.TokenExpiredException;
import ru.discloud.auth.exception.TokenInvalidException;
import ru.discloud.auth.exception.UserCredentialsException;
import ru.discloud.auth.service.AppTokenService;
import ru.discloud.auth.web.model.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth/oauth")
public class AppTokenController {
  private final AppTokenService appTokenService;

  @Autowired
  public AppTokenController(AppTokenService appTokenService) {
    this.appTokenService = appTokenService;
  }

  @CrossOrigin(origins = "http://localhost")
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public AppTokenCodeResponse createToken(@Valid @RequestBody AppTokenInitialRequest tokenRequest) throws UserCredentialsException {
    AppToken token = this.appTokenService.create(tokenRequest);
    return new AppTokenCodeResponse(token.getAppCode(), tokenRequest.getRedirectUrl());
  }

  @RequestMapping(value = "/token", method = RequestMethod.POST)
  public AppTokenResponse getToken(@Valid @RequestBody AppTokenCodeRequest codeRequest) throws AppCredentialsException {
    return new AppTokenResponse(this.appTokenService.findByAppCode(codeRequest));
  }

  @RequestMapping(value = "/token/refresh", method = RequestMethod.POST)
  public AppTokenResponse refreshToken(@Valid @RequestBody AppTokenRefreshRequest refreshRequest) throws Exception {
    return new AppTokenResponse(this.appTokenService.refresh(refreshRequest));
  }

  @RequestMapping(value = "/token/check", method = RequestMethod.POST)
  public UserResponse checkToken(@Valid @RequestBody AppTokenRequest tokenRequest)
      throws TokenExpiredException, TokenInvalidException {
    return new UserResponse(this.appTokenService.checkAccessToken(tokenRequest));
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RequestMapping(value = "/token", method = RequestMethod.DELETE)
  public void deleteToken(@Valid @RequestBody AppTokenRequest tokenRequest) throws TokenInvalidException {
    this.appTokenService.deleteToken(tokenRequest);
  }
}
