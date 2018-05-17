package ru.discloud.user.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.discloud.user.domain.Country;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryResponse {
    private String key;
    private String name;
    private String flag;

    public CountryResponse(Country country) {
        this.key = country.getKey();
        this.name = country.getName();
        this.flag = country.getFlag();
    }
}