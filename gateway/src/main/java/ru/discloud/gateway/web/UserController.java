package ru.discloud.gateway.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.service.UserService;
import ru.discloud.gateway.web.model.UserPageResponse;
import ru.discloud.gateway.web.model.UserResponse;

@RestController
@RequestMapping("/api/user")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(path = "/")
  public Mono<UserPageResponse> getUsers() throws Exception {
    return userService.getUsers();
  }

  @GetMapping(path = "/{id}")
  public Mono<UserResponse> getUser(@PathVariable Long id) throws Exception {
    return userService.getUserById(id).map(UserResponse::new);
  }
}
