package ru.discloud.auth.web.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.discloud.shared.MemberOf;

import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
public class AppRequest {
    @NotEmpty(message = "Phone not provided")
    private String name;
    @MemberOf(value = "eternal,extend", message = "tokenType isn't recognized")
    private String tokenType;
    @MemberOf(value = "readonly,readwrite", message = "tokenPermission isn't recognized")
    private String tokenPermission;
}
