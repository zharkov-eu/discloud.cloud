package ru.discloud.shared.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ru.discloud.shared.web.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ProxyAuthInterceptor extends HandlerInterceptorAdapter {
  private final ProxyAuthProvider authProvider;
  private ObjectMapper mapper = new ObjectMapper();

  public ProxyAuthInterceptor() {
    this.authProvider = new ProxyAuthProvider();
  }

  public ProxyAuthInterceptor(List<ProxyUser> users, String secret) {
    this.authProvider = new ProxyAuthProvider(users, secret);
  }

  protected void setAuthProviderCredentials(List<ProxyUser> users, String secret) {
    this.authProvider.setUsers(users);
    this.authProvider.setSecret(secret);
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String authorization = request.getHeader("Authorization");
    if (authorization != null && authorization.startsWith("Basic")) {
      authProvider.userByBasicAuthorization(authorization)
          .ifPresent(client -> {
            ProxyAuthToken token = authProvider.createAccessToken(client);
            response.setHeader("X-Auth-Token", token.getToken());
            response.setHeader("X-Auth-Expire", token.getExpire().toString());
          });
      if (response.getHeader("X-Auth-Token") != null) return true;
    }
    if (authorization != null && authorization.startsWith("Bearer")) {
      if (authProvider.checkAccessToken(authorization.substring("Bearer".length()).trim())) return true;
    }
    response.setStatus(HttpStatus.PROXY_AUTHENTICATION_REQUIRED.value());
    response.setHeader("Content-Type", "application/json");
    response.getWriter().write(mapper.writeValueAsString(
        new ErrorResponse(new ProxyBadCredentialsException(), HttpStatus.PROXY_AUTHENTICATION_REQUIRED))
    );
    return false;
  }
}
