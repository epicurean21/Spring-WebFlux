package com.example.webflux.websocket.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Client {
    private static final AtomicInteger MESSAGE_ID;

    static {
        MESSAGE_ID = new AtomicInteger(0);
    }

    private Disposable subscription;

    public void start(final WebSocketClient webSocketClient, final URI uri) {
        subscription = webSocketClient.execute(uri, this::doLogic).subscribe();
    }

    public void stop() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }

        log.info("Client stopped.");
    }

    public Mono<Void> doLogic(final WebSocketSession session) {
        return sendMessage(session)
                .thenMany(receiveAll(session))
                .then();
    }

    private Flux<WebSocketMessage> receiveAll(final WebSocketSession session) {
        return session.receive()
                .doOnNext(message -> log.info("Client({}) -> received: [{}]", session.getId(), message.getPayloadAsText()));
    }

    private Mono<Void> sendMessage(final WebSocketSession session) {
        final String message = "Test message " + MESSAGE_ID.getAndIncrement();

        return
                Mono.fromRunnable(() -> log.info("Client Connected id: {}", session.getId()))
                        .then(session.send(Mono.fromCallable(() -> session.textMessage(message))))
                        .then(Mono.fromRunnable(() -> log.info("Client({}) -> sent: [{}]", session.getId(), message)));
    }
}
