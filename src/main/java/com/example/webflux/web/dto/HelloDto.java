package com.example.webflux.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HelloDto {
    private String message;

    @Builder
    public HelloDto(String message) {
        this.message = message;
    }
}
