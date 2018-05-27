package ru.discloud.statistics.service;

import ru.discloud.shared.web.statistic.UploadRequest;

public interface UploadService {
  void save(UploadRequest uploadRequest);
}
