package com.example.webflux.websocket.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class WebSocketTestHandler implements org.springframework.web.reactive.socket.WebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new LinkedHashMap<>();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.receive()
                .doOnNext(message -> log.info("client connected id: {}", session.getId()))
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(message -> log.info("received message from client: {} ", message))
                .flatMap(message -> {
                    this.sessions.put(session.getId(), session);
                    return this.broadcast(message);
                })
                .then();
    }

    public Mono<Void> broadcast(String response) {
        return Mono.create(sink -> sessions.values().forEach(
                session -> sink.success(
                        session.send(Mono.just(session.textMessage(response))).subscribe())
        )).then();
    }
}
