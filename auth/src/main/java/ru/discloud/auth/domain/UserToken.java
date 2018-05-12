package ru.discloud.auth.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class UserToken {
    private Integer userId;
    private String deviceId;
    private String accessToken;
    private Date accessExpires;
    private String refreshToken;
    private Date refreshExpires;
}

