package ru.discloud.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.discloud.user.domain.Country;

public interface CountryRepository extends JpaRepository<Country, String> {
}
