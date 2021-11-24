package com.example.webflux.web.controller;

import com.example.webflux.web.dto.HelloDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequestMapping(value = "/test")
@RestController
public class TestController {

    public Mono<String> getRequestEcho(@RequestBody HelloDto helloDto) {
        return Mono.just(helloDto.getMessage());
    }

}
