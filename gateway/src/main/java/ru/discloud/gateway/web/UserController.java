package ru.discloud.gateway.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.domain.User;
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
  public Mono<UserPageResponse> getUsers() {
    return userService.getUsers();
  }

  @GetMapping(path = "/{id}")
  public Mono<UserResponse> getUser(@PathVariable Long id) {
    return userService.getUserById(id).map(UserResponse::new);
  }

  @GetMapping(path = "/by/")
  public Mono<UserResponse> getUser(@RequestParam(name = "username", required = false) String username,
                                    @RequestParam(name = "email", required = false) String email,
                                    @RequestParam(name = "phone", required = false) String phone) {
    return userService.getUserBy(username, email, phone).map(UserResponse::new);
  }
}
