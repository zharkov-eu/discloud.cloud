package ru.discloud.cloud.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.discloud.cloud.domain.User;
import ru.discloud.cloud.web.model.UserRequest;

public interface UserService {

    Page<User> findAll(Pageable pageable);

    User findById(Long id);

    User findByEmail(String email);

    User findByPhone(String phone);

    User findByUsername(String username);

    Long save(UserRequest userRequest);

    User update(Long id, UserRequest userRequest);

    void delete(Long id);
}