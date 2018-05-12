package ru.discloud.auth.web.model;

import lombok.Data;

@Data
public class UserTokenRemoveRequest {
    Integer userId;
    String deviceId;
}
