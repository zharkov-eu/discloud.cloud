package ru.discloud.auth.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.discloud.auth.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
}
