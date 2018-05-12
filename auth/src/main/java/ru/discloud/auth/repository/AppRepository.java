package ru.discloud.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.discloud.auth.domain.App;

import java.util.Optional;

@Repository
public interface AppRepository extends JpaRepository<App, Integer> {

    Optional<App> findByName(String name);
}
