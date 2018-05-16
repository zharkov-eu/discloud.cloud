package ru.discloud.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.discloud.shared.ReverseLookupEnum;
import ru.discloud.user.domain.User;
import ru.discloud.user.domain.UserPrivileges;
import ru.discloud.user.repository.UserRepository;
import ru.discloud.user.web.model.UserRequest;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ReverseLookupEnum<UserPrivileges> userPrivileges = new ReverseLookupEnum<>(UserPrivileges.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User findById(Long id) throws EntityNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User '{" + id + "}' not found"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User by email '{" + email + "}' not found"));
    }

    @Override
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new EntityNotFoundException("User by phone '{" + phone + "}' not found"));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User by username '{" + username + "}' not found"));
    }

    @Override
    public User save(UserRequest userRequest) {
        User existingUser = userRepository.findByUsername(userRequest.getUsername()).orElse(null);
        if (existingUser != null) {
            throw new EntityExistsException("User with username '{" + userRequest.getUsername() + "}' already exist");
        }

        User user = new User()
                .setUsername(userRequest.getUsername())
                .setEmail(userRequest.getEmail())
                .setPhone(userRequest.getPhone())
                .setPrivileges(userPrivileges.get(userRequest.getUserPrivileges()))
                .setQuota(userRequest.getQuota());

        return userRepository.save(user);
    }

    @Override
    public User update(Long id, UserRequest userRequest) {
        User user = findById(id);
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
