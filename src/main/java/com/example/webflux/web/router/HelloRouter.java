package com.example.webflux.web.router;

import com.example.webflux.web.handler.HelloHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class HelloRouter {

    @Bean
    public RouterFunction<ServerResponse> helloRouter(HelloHandler helloHandler) {
        return RouterFunctions.route(RequestPredicates.POST("/hello")
                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), helloHandler::echoHello);
    }
}
