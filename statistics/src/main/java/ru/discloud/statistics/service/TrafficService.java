package ru.discloud.statistics.service;

import ru.discloud.statistics.domain.Traffic;
import ru.discloud.statistics.web.model.TrafficRequest;

public interface TrafficService {
    Traffic save(TrafficRequest trafficRequest);
}