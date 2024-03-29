package com.example.webflux.mono.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    @Async
    public String work(String req) {
        return req + "/asyncwork";
    }
}
