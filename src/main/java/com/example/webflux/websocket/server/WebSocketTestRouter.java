package com.example.webflux.websocket.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketTestRouter {
    private final WebSocketHandler webSocketHandler;

    public WebSocketTestRouter(WebSocketTestHandler webSocketTestHandler) {
        this.webSocketHandler = webSocketTestHandler;
    }

    @Bean
    public HandlerMapping webSocketRouterHandlerMapping() {
        Map<String, WebSocketHandler> connections = new HashMap<>();
        connections.put("/v1/websocket", webSocketHandler);
        return new SimpleUrlHandlerMapping(connections, -1);
    }
}
