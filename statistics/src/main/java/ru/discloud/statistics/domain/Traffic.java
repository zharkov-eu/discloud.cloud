package ru.discloud.statistics.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Traffic {
    private String serviceUuid;
    private Integer income;
    private Integer outcome;
}
