package ru.discloud.statistics.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.discloud.shared.web.statistic.TrafficRequest;
import ru.discloud.statistics.service.TrafficService;

@Service
public class TrafficQueueHandler {
  private final QueueHandler<TrafficRequest> trafficQueueHandler;
  private final TrafficService trafficService;

  @Autowired
  public TrafficQueueHandler(RedisTemplate<String, String> redisTemplate, TrafficService trafficService) {
    this.trafficQueueHandler = new QueueHandler<>(redisTemplate, TrafficRequest.class);
    this.trafficService = trafficService;
  }

  public void handle() {
    trafficQueueHandler.handle(request -> {
      if (request != null) trafficService.save(request);
    });
  }
}
