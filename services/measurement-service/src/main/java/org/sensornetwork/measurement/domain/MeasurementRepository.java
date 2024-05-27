package org.sensornetwork.measurement.domain;

import reactor.core.publisher.Flux;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MeasurementRepository extends ReactiveMongoRepository<Measurement, String> {
    Flux<Measurement> findByDeviceId(String deviceId);
}