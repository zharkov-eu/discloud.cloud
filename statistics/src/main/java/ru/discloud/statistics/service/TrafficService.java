package ru.discloud.statistics.service;

import ru.discloud.shared.web.statistic.TrafficRequest;

public interface TrafficService {
  void save(TrafficRequest trafficRequest);
}
