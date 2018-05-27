package ru.discloud.auth.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.discloud.auth.domain.AppToken;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppTokenResponse {
  private String accessToken;
  private Date accessExpires;
  private String refreshToken;
  private Date refreshExpires;

  public AppTokenResponse(AppToken appToken) {
    this.accessToken = appToken.getAccessToken();
    this.accessExpires = appToken.getAccessExpires();
    this.refreshToken = appToken.getRefreshToken();
    this.refreshExpires = appToken.getRefreshExpires();
  }
}
