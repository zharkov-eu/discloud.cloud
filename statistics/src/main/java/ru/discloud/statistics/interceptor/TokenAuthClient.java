package ru.discloud.statistics.interceptor;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.discloud.shared.auth.AuthClient;

@Data
@Accessors(chain = true)
class TokenAuthClient implements AuthClient {
    private String username;
    private String password;
}
