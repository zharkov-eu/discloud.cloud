package ru.discloud.auth.web.model;

import lombok.Data;
import ru.discloud.shared.MemberOf;

import javax.validation.constraints.NotEmpty;

@Data
public class AppRequest {
    @NotEmpty(message = "Phone not provided")
    private String name;
    @MemberOf(value = "eternal,extend", message = "tokenType isn't recognized")
    private String tokenType;
    @MemberOf(value = "readonly,readwrite", message = "tokenPermission isn't recognized")
    private String tokenPermission;
}
