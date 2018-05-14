package ru.discloud.statistics.web.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class TrafficRequest {
    @NotEmpty
    private String serviceUuid;
    @NotNull
    @PositiveOrZero
    private Integer income;
    @NotNull
    @PositiveOrZero
    private Integer outcome;
}
