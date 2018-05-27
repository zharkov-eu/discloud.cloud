package ru.discloud.statistics.service;

import ru.discloud.shared.web.statistic.UserRequest;

public interface UserService {
  void save(UserRequest userRequest);
}
