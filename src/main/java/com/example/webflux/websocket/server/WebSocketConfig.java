package com.example.webflux.websocket.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Configuration
public class WebSocketConfig {
    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        HandshakeWebSocketService handshakeWebSocketService = new HandshakeWebSocketService();

        return new WebSocketHandlerAdapter(handshakeWebSocketService) {
            /**
             * WebSocketHandlerAdapter가 handlerMapping에 의해 Handler를
             * 호출하기 전 Handshake Info등을 Custom 해보자.
             */
            @Override
            public Mono<HandlerResult> handle(ServerWebExchange exchange, Object handler) {
                log.info("WebSocketHandlerAdapter Attributes");
                exchange.getSession()
                        .map(session -> {
                            log.info("WebSocketSession id: {}", session.getId());
                            return session;
                        }).subscribe();

                Map<String, Object> attributes = exchange.getAttributes();
                attributes.values().forEach(atr -> log.info(atr.toString()));

                return super.handle(exchange, handler);
            }
        };
    }
}