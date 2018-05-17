package ru.discloud.statistics.service;

import ru.discloud.statistics.web.model.UploadRequest;

public interface UploadService {
    void save(UploadRequest uploadRequest);
}
