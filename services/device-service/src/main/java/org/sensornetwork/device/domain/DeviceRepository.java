package org.sensornetwork.device.domain;

import reactor.core.publisher.Flux;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DeviceRepository extends ReactiveMongoRepository<Device, String> {
    Flux<Device> findAllByUserIdOrderByCreatedAtDesc(String userId);

    Flux<Device> findAllByLocation_LatitudeAndLocation_Longitude(double lat, double lon);
}