package ru.discloud.auth.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.discloud.auth.domain.App;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppResponse {
    private Integer id;
    private String name;
    private String secret;
    private String tokenType;
    private String tokenPermission;

    public AppResponse(App app) {
        this.id = app.getId();
        this.name = app.getName();
        this.secret = app.getSecret();
        this.tokenType = app.getTokenType().toString();
        this.tokenPermission = app.getTokenPermission().toString();
    }
}
