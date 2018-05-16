package ru.discloud.shared.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthToken {
    String token;
    Long expire;
}
