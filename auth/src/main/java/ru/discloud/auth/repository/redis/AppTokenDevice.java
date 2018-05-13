package ru.discloud.auth.repository.redis;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppTokenDevice {
    private Integer appId;
    private Long userId;
    private String deviceId;
}