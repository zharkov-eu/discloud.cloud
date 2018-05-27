package ru.discloud.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.discloud.shared.ReverseLookupEnum;
import ru.discloud.shared.UserPrivileges;
import ru.discloud.user.domain.User;
import ru.discloud.user.integration.mailgun.MailClient;
import ru.discloud.user.mail.UserSignupMessage;
import ru.discloud.user.repository.UserRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final MailClient mailClient;
  private final ReverseLookupEnum<UserPrivileges> userPrivileges = new ReverseLookupEnum<>(UserPrivileges.class);
  private final ExecutorService executor;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, MailClient mailClient) {
    this.executor = Executors.newFixedThreadPool(1);
    this.userRepository = userRepository;
    this.mailClient = mailClient;
  }

  @Override
  public Page<User> findAll(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  @Override
  public User findById(Long id) throws EntityNotFoundException {
    return userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("user '{" + id + "}' not found"));
  }

  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("user by email '{" + email + "}' not found"));
  }

  @Override
  public User findByPhone(String phone) {
    return userRepository.findByPhone(phone)
        .orElseThrow(() -> new EntityNotFoundException("user by phone '{" + phone + "}' not found"));
  }

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("user by username '{" + username + "}' not found"));
  }

  @Override
  public User save(UserRequest userRequest) {
    User existingUser = userRepository.findByUsername(userRequest.getUsername()).orElse(null);
    if (existingUser != null) {
      throw new EntityExistsException("user with username '{" + userRequest.getUsername() + "}' already exist");
    }

    User user = new User()
        .setUsername(userRequest.getUsername())
        .setEmail(userRequest.getEmail())
        .setPhone(userRequest.getPhone())
        .setPrivileges(userPrivileges.get(userRequest.getUserPrivileges()))
        .setQuota(userRequest.getQuota());

    User savedUser = userRepository.save(user);

    if (user.getEmail() != null) {
      this.executor.execute(() -> {
        UserSignupMessage message = new UserSignupMessage(user.getEmail());
        try {
          mailClient.sendMessage(message).thenAccept(sendMessageResponse -> {
            if (sendMessageResponse == null) return;
            savedUser.setSignupMessage(sendMessageResponse.getId());
            userRepository.save(savedUser);
          }).join();
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
      });
    }

    return savedUser;
  }

  @Override
  public User update(Long id, UserRequest userRequest) {
    User user = findById(id);
    return userRepository.save(user);
  }

  @Override
  public void delete(Long id) {
    User user = findById(id);
    userRepository.delete(user);
  }
}
