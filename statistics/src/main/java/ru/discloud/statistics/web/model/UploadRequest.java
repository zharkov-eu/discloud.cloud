package ru.discloud.statistics.web.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class UploadRequest {
    @NotEmpty
    private String username;
    @NonNull
    private Boolean encrypted;
    @NotNull
    @Positive
    private Integer size;
}
