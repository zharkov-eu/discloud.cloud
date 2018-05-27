package ru.discloud.statistics.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.discloud.shared.web.statistic.UserRequest;
import ru.discloud.statistics.queue.UserQueueHandler;
import ru.discloud.statistics.service.UserService;
import ru.discloud.statistics.web.model.AckResponse;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/statistics/user")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService, UserQueueHandler queueHandler) {
    this.userService = userService;
    queueHandler.handle();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(method = RequestMethod.POST)
  public AckResponse save(@Valid @RequestBody UserRequest request) {
    userService.save(request);
    return new AckResponse(true);
  }
}
