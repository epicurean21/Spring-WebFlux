package com.example.webflux.websocket.client;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
public class ClientConfig implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final WebSocketClient webSocketClient = new ReactorNettyWebSocketClient();

        final Client client = new Client();

        client.start(webSocketClient, getURI());

    }

    private URI getURI() {
        try {
            return new URI("ws://localhost:" + 8080 + "/ws");
        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
