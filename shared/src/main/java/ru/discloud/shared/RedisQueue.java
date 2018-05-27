package ru.discloud.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;

public class RedisQueue<T> {
  private static final String DELIMITER = ":::";

  private final ObjectMapper mapper = new ObjectMapper();

  private final RedisTemplate<String, String> redisTemplate;
  private final Class<T> typeParameterClass;
  private final String queueName;
  private final String processingQueueName;
  private final String fallbackQueueName;

  public RedisQueue(RedisTemplate<String, String> redisTemplate,
                    Class<T> typeParameterClass,
                    String queueName) {
    this.typeParameterClass = typeParameterClass;
    this.redisTemplate = redisTemplate;
    this.queueName = queueName;
    this.processingQueueName = queueName + DELIMITER + "processing";
    this.fallbackQueueName = queueName + DELIMITER + "fallback";
  }

  public void enqueue(T element) throws JsonProcessingException {
    redisTemplate.boundListOps(queueName).leftPush(mapper.writeValueAsString(element));
  }

  public T ack() throws IOException {
    return mapper.readValue(redisTemplate.opsForList().rightPopAndLeftPush(queueName, processingQueueName), typeParameterClass);
  }

  public void peek() {
    redisTemplate.opsForList().rightPop(processingQueueName);
  }

  public void bury() throws IOException {
    redisTemplate.opsForList().rightPopAndLeftPush(processingQueueName, fallbackQueueName);
  }
}
