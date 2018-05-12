package ru.discloud.auth.web.model;

import lombok.Data;

@Data
public class AppRequest {
    private String name;
    private String tokenType;
    private String tokenPermission;
}
