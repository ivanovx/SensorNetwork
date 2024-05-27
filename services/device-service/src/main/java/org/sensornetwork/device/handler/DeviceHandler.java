package org.sensornetwork.device.handler;

import org.sensornetwork.device.domain.Device;
import org.sensornetwork.device.domain.DeviceRepository;
import org.sensornetwork.device.domain.DeviceRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class DeviceHandler {
    private final DeviceRepository deviceRepository;

    public DeviceHandler(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Mono<ServerResponse> getAllDevices(ServerRequest request) {
       return ServerResponse.ok().body(deviceRepository.findAll(), Device.class);
    }

    public Mono<ServerResponse> getAllDevicesByUser(ServerRequest request) {
        //request.principal().map(principal -> (Jwt) principal);

        return ReactiveSecurityContextHolder.getContext()
                .map(context -> (Jwt) context.getAuthentication().getPrincipal())
                .map(jwt -> {
                    String userId = jwt.getClaimAsString("userId");

                    return deviceRepository.findAllByUserId(userId);
                })
                .flatMap(devices ->  ServerResponse.ok().body(devices, Device.class));
    }

    public Mono<ServerResponse> getDevice(ServerRequest request) {
        String deviceId = request.pathVariable("deviceId");

        return deviceRepository
                .findById(deviceId)
                .flatMap(device -> ServerResponse.ok().body(device, Device.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createDevice(ServerRequest request) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> (Jwt) context.getAuthentication().getPrincipal())
                .flatMap(jwt -> {
                    String userId = jwt.getClaimAsString("userId");

                    return request.bodyToMono(DeviceRequest.class).flatMap(deviceRequest -> {
                        Device device = Device.builder()
                                .userId(userId)
                                .name(deviceRequest.name())
                                .updatedAt(null)
                                .createdAt(LocalDateTime.now())
                                .description(deviceRequest.description())
                                .location(deviceRequest.location())
                                .build();

                        return deviceRepository.save(device).flatMap(d -> ServerResponse.status(201).body(d, Device.class));
                    });
                });
    }

    public Mono<ServerResponse> updateDevice(ServerRequest request) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> (Jwt) context.getAuthentication().getPrincipal())
                .flatMap(jwt -> {
                    String userId = jwt.getClaimAsString("userId");

                    return request.bodyToMono(DeviceRequest.class).flatMap(deviceRequest -> {
                        Device device = Device.builder()
                                .userId(userId)
                                .name(deviceRequest.name())
                                .updatedAt(null)
                                .createdAt(LocalDateTime.now())
                                .description(deviceRequest.description())
                                .location(deviceRequest.location())
                                .build();

                        return deviceRepository.save(device).flatMap(d -> ServerResponse.status(201).body(d, Device.class));
                    });
                });
    }

    public Mono<ServerResponse> deleteDevice(ServerRequest request) {
        String deviceId = request.pathVariable("deviceId");

        return deviceRepository.findById(deviceId)
                .flatMap(device -> {
                    deviceRepository.deleteById(device.getId());

                    return ServerResponse.ok().body(device, Device.class);
                });
    }
}