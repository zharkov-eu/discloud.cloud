package ru.discloud.statistics.service;

import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.discloud.shared.web.statistic.TrafficRequest;
import ru.discloud.statistics.domain.Traffic;
import ru.discloud.statistics.repository.InfluxDBRepository;

import java.util.concurrent.TimeUnit;

@Service
public class TrafficServiceImpl implements TrafficService {
  private final InfluxDBRepository repository;

  @Autowired
  public TrafficServiceImpl(InfluxDBRepository influxDBRepository) {
    this.repository = influxDBRepository;
  }

  @Override
  public void save(TrafficRequest trafficRequest) {
    Traffic traffic = new Traffic()
        .setServiceUuid(trafficRequest.getServiceUuid())
        .setIncome(trafficRequest.getIncome())
        .setOutcome(trafficRequest.getOutcome());
    Point point = Point.measurement("traffic")
        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        .tag("service", traffic.getServiceUuid())
        .addField("income", traffic.getIncome())
        .addField("outcome", traffic.getOutcome())
        .build();
    repository.write(point);
  }
}
