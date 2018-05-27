package ru.discloud.statistics.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.discloud.shared.web.statistic.UserRequest;
import ru.discloud.statistics.service.UserService;

@Service
public class UserQueueHandler {
  private final QueueHandler<UserRequest> userQueueHandler;
  private final UserService userService;

  @Autowired
  public UserQueueHandler(RedisTemplate<String, String> redisTemplate, UserService userService) {
    this.userQueueHandler = new QueueHandler<>(redisTemplate, UserRequest.class);
    this.userService = userService;
  }

  public void handle() {
    userQueueHandler.handle(request -> {
      if (request != null) userService.save(request);
    });
  }
}
