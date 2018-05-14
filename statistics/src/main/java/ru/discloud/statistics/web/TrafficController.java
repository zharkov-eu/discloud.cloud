package ru.discloud.statistics.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.discloud.statistics.service.TrafficService;
import ru.discloud.statistics.web.model.AckResponse;
import ru.discloud.statistics.web.model.TrafficRequest;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/statistics/traffic")
public class TrafficController {
    private final TrafficService trafficService;

    @Autowired
    public TrafficController(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public AckResponse save(@Valid @RequestBody TrafficRequest request) {
        trafficService.save(request);
        return new AckResponse(true);
    }
}
