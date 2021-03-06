package ru.discloud.gateway.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.discloud.gateway.service.UserService;
import ru.discloud.gateway.web.model.UserPageResponse;
import ru.discloud.gateway.web.model.UserRequest;
import ru.discloud.gateway.web.model.UserResponse;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;

@RestController
@RequestMapping("/api/user")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping()
  public Mono<UserResponse> createUser(@Valid  @RequestBody UserRequest userRequest)
      throws ValidationException, JsonProcessingException {
    return userService.createUser(userRequest).map(UserResponse::new);
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

  @DeleteMapping(path = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteUser(@PathVariable Long id) {
    return userService.deleteUser(id);
  }
}
