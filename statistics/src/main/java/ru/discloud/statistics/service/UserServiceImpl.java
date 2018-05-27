package ru.discloud.statistics.service;

import org.influxdb.dto.Point;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.discloud.shared.web.statistic.UserRequest;
import ru.discloud.statistics.domain.User;
import ru.discloud.statistics.repository.InfluxDBRepository;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
  private final InfluxDBRepository repository;

  @Autowired
  public UserServiceImpl(InfluxDBRepository repository) {
    this.repository = repository;
  }

  @Override
  public void save(@NotNull UserRequest userRequest) {
    User user = new User()
        .setUsername(userRequest.getUsername())
        .setUtmLabel(userRequest.getUtm());
    Point.Builder pointBuilder = Point.measurement("user")
        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        .addField("username", user.getUsername());
    if (user.getUtmLabel() != null) {
      pointBuilder
          .tag("utm_source", user.getUtmLabel().getSource())
          .tag("utm_campaign", user.getUtmLabel().getCampaign())
          .tag("utm_content", user.getUtmLabel().getContent());
    }
    Point point = pointBuilder.build();
    repository.write(point);
  }
}
