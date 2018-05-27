package ru.discloud.gateway.repository.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.discloud.shared.web.statistic.UserRequest;

@Repository
public class UserStatisticQueue {
  private final StatisticQueue<UserRequest> userRequestStatisticQueue;

  @Autowired
  public UserStatisticQueue(RedisTemplate<String, String> redisTemplate) {
    userRequestStatisticQueue = new StatisticQueue<>(redisTemplate, UserRequest.class);
  }

  public void enqueue(UserRequest request) {
    this.userRequestStatisticQueue.enqueue(request);
  }
}
