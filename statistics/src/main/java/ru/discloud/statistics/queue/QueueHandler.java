package ru.discloud.statistics.queue;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import ru.discloud.shared.RedisQueue;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
class QueueHandler<T> {
  private static final String DELIMITER = ":::";

  private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
  private final RedisQueue<T> queue;

  interface QueueCallback<T> {
    void cb(T entity);
  }

  QueueHandler(RedisTemplate<String, String> redisTemplate, Class<T> typeParameterClass) {
    String queueName = "statistic" + DELIMITER + typeParameterClass.getSimpleName().toLowerCase();
    this.queue = new RedisQueue<>(redisTemplate, typeParameterClass, queueName);
  }

  void handle(QueueCallback<T> callback) {
    executorService.scheduleWithFixedDelay(() -> {
      try {
        T entity = queue.ack();
        if (entity == null) Thread.sleep(500L);
        else {
          callback.cb(entity);
          queue.peek();
        }
      } catch (Exception e) {
        queue.bury();
        log.error(e.getClass().getSimpleName() + " : " + e.getMessage());
      }
    }, 0L, 10L, TimeUnit.MILLISECONDS);
  }
}
