package ru.discloud.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.discloud.shared.web.user.UserRequest;
import ru.discloud.user.domain.User;

public interface UserService {
  Page<User> findAll(Pageable pageable);

  User findById(Long id);

  User findByEmail(String email);

  User findByPhone(String phone);

  User findByUsername(String username);

  User save(UserRequest userRequest) throws JsonProcessingException;

  User update(Long id, UserRequest userRequest);

  void delete(Long id);
}