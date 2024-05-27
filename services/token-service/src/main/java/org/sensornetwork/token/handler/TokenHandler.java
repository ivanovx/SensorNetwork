package org.sensornetwork.token.handler;

import java.time.LocalDateTime;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import org.sensornetwork.common.TokenGenerator;
import org.sensornetwork.common.request.TokenVerifyRequest;

import org.sensornetwork.token.domain.Token;
import org.sensornetwork.token.domain.TokenRepository;

@Component
public class TokenHandler {
    private final TokenRepository tokenRepository;

    public TokenHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Mono<ServerResponse> getDeviceToken(ServerRequest request) {
        String deviceId = request.pathVariable("deviceId");

        return tokenRepository
                .findTokenByDeviceId(deviceId)
                .flatMap(token -> ServerResponse.ok().body(token, Token.class))
                .switchIfEmpty(ServerResponse.ok().body("Token", String.class));
    }

    public Mono<ServerResponse> createDeviceToken(ServerRequest request) {
        String deviceId = request.pathVariable("deviceId");

        Token token = Token.builder()
                .deviceId(deviceId)
                .value(TokenGenerator.createToken(deviceId))
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusYears(1))
                .build();

        return tokenRepository.save(token).flatMap(t->  ServerResponse.status(201).body(t, Token.class));
    }

    /*public Mono<ServerResponse> verifyToken(ServerRequest request) {
        return request.bodyToMono(TokenVerifyRequest.class)
                .flatMap(body -> tokenRepository.findByDeviceId(body.deviceId()).flatMap(token -> {
                    if (LocalDateTime.now().isAfter(token.getExpiredAt())) {
                        return ServerResponse.badRequest().bodyValue("EXPIRED");
                    }

                    if (!token.getValue().equals(body.token())) {
                        return ServerResponse.badRequest().bodyValue("NOT_VALID");
                    }

                    return ServerResponse.ok().bodyValue("VALID");
                }));
    }*/
}
