package ru.discloud.statistics.repository;

import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InfluxDBRepository {
    private final InfluxDBTemplate<Point> influxDBTemplate;

    @Autowired
    public InfluxDBRepository(InfluxDBTemplate<Point> influxDBTemplate) {
        this.influxDBTemplate = influxDBTemplate;
        this.influxDBTemplate.createDatabase();
    }

    public void write(Point... point) {
        influxDBTemplate.write(point);
    }
}
