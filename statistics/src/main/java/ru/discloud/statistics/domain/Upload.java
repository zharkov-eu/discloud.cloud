package ru.discloud.statistics.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Upload {
    private String username;
    private Boolean encrypted;
    private Integer size;
}
