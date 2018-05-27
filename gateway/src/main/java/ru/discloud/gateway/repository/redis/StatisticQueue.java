package ru.discloud.gateway.repository.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.redis.core.RedisTemplate;
import ru.discloud.shared.RedisQueue;

class StatisticQueue<T> {
  private final static String DELIMITER = "::";

  private final RedisQueue<T> queue;

  StatisticQueue(RedisTemplate<String, String> redisTemplate, Class<T> typeParameterClass) {
    String queueName = "statistic" + DELIMITER + typeParameterClass.getSimpleName().toLowerCase();
    this.queue = new RedisQueue<>(redisTemplate, typeParameterClass, queueName);
  }

  void enqueue(T request) {
    try {
      this.queue.enqueue(request);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
