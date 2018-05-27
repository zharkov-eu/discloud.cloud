package ru.discloud.statistics.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.discloud.shared.web.statistic.TrafficRequest;
import ru.discloud.statistics.queue.TrafficQueueHandler;
import ru.discloud.statistics.service.TrafficService;
import ru.discloud.statistics.web.model.AckResponse;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/statistics/traffic")
public class TrafficController {
  private final TrafficService trafficService;

  @Autowired
  public TrafficController(TrafficService trafficService, TrafficQueueHandler queueHandler) {
    this.trafficService = trafficService;
    queueHandler.handle();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(method = RequestMethod.POST)
  public AckResponse save(@Valid @RequestBody TrafficRequest request) {
    trafficService.save(request);
    return new AckResponse(true);
  }
}
