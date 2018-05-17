package ru.discloud.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.discloud.user.domain.User;
import ru.discloud.user.web.model.UserRequest;

public interface UserService {
    Page<User> findAll(Pageable pageable);

    User findById(Long id);

    User findByEmail(String email);

    User findByPhone(String phone);

    User findByUsername(String username);

    User save(UserRequest userRequest);

    User update(Long id, UserRequest userRequest);

    void delete(Long id);
}