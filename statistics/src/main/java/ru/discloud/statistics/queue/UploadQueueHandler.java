package ru.discloud.statistics.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.discloud.shared.web.statistic.UploadRequest;
import ru.discloud.statistics.service.UploadService;

@Service
public class UploadQueueHandler {
  private final QueueHandler<UploadRequest> uploadQueueHandler;
  private final UploadService uploadService;

  @Autowired
  public UploadQueueHandler(RedisTemplate<String, String> redisTemplate, UploadService uploadService) {
    this.uploadQueueHandler = new QueueHandler<>(redisTemplate, UploadRequest.class);
    this.uploadService = uploadService;
  }

  public void handle() {
    uploadQueueHandler.handle(request -> {
      if (request != null) uploadService.save(request);
    });
  }
}
