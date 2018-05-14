package ru.discloud.user.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.discloud.user.domain.Country;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryResponse {
    private String Key;
    private String Name;
    private String Flag;

    public CountryResponse(Country country) {
        this.Key = country.getKey();
        this.Name = country.getName();
        this.Flag = country.getFlag();
    }
}