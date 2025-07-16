package com.danila.synthetichumancorestarter.web;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(Instant timestamp, int code, String message, String details) {
    public static ApiError of(int code, String message, String details) {
        return new ApiError(Instant.now(), code, message, details);
    }
}
