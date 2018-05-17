package ru.discloud.statistics.service;

import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.discloud.statistics.domain.Upload;
import ru.discloud.statistics.repository.InfluxDBRepository;
import ru.discloud.statistics.web.model.UploadRequest;

import java.util.concurrent.TimeUnit;

@Service
public class UploadServiceImpl implements UploadService {
    private final InfluxDBRepository influxDBRepository;

    @Autowired
    public UploadServiceImpl(InfluxDBRepository influxDBRepository) {
        this.influxDBRepository = influxDBRepository;
    }

    @Override
    public void save(UploadRequest uploadRequest) {
        Upload upload = new Upload()
                .setUsername(uploadRequest.getUsername())
                .setEncrypted(uploadRequest.getEncrypted())
                .setSize(uploadRequest.getSize());
        Point point = Point.measurement("upload")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("username", upload.getUsername())
                .tag("encrypted", upload.getEncrypted().toString())
                .addField("size", upload.getSize())
                .build();
        influxDBRepository.write(point);
    }
}
