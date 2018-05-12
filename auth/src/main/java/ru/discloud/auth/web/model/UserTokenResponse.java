package ru.discloud.auth.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.discloud.auth.domain.UserToken;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTokenResponse {
    private String accessToken;
    private Date accessExpires;
    private String refreshToken;
    private Date refreshExpires;

    public UserTokenResponse(UserToken userToken) {
        this.accessToken = userToken.getAccessToken();
        this.accessExpires = userToken.getAccessExpires();
        this.refreshToken = userToken.getRefreshToken();
        this.refreshExpires = userToken.getRefreshExpires();
    }
}
