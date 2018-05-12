package ru.discloud.auth.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.discloud.auth.domain.User;
import ru.discloud.auth.exception.UserCredentialsException;
import ru.discloud.auth.repository.jpa.UserRepository;
import ru.discloud.auth.web.model.UserRequest;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username = " + username + " not found"));
    }

    @Override
    public User checkCredentials(String username, String password) throws UserCredentialsException {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User with username = " + username + " not found"));
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update((password + user.getSalt()).getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(messageDigest.digest()).equals(user.getPassword()) ? user : null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserCredentialsException();
        }
    }

    @Override
    public User save(UserRequest userRequest) throws Exception {
        User existingUser = userRepository.findByUsername(userRequest.getUsername()).orElse(null);
        if (existingUser != null) {
            throw new EntityExistsException("User with username '{" + userRequest.getUsername() + "}' already exist");
        }

        String salt = RandomStringUtils.randomAlphanumeric(10);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update((userRequest.getPassword() + salt).getBytes("UTF-8"));
        User user = new User()
                .setId(userRequest.getId())
                .setUsername(userRequest.getUsername())
                .setPassword(DatatypeConverter.printHexBinary(messageDigest.digest()))
                .setSalt(salt);
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
