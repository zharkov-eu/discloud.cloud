package ru.discloud.auth.repository.redis;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTokenDevice {
    private Integer userId;
    private String deviceId;
}
