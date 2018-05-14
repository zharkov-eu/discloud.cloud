package ru.discloud.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.discloud.user.domain.Country;
import ru.discloud.user.repository.CountryRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Country findByKey(String key) {
        return countryRepository.findById(key)
                .orElseThrow(() -> new EntityNotFoundException("Country '{" + key + "}' not found"));
    }

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }
}
