package ru.discloud.user.service;

import ru.discloud.user.domain.Country;

import java.util.List;

public interface CountryService {
    Country findByKey(String key);
    List<Country> findAll();
}
