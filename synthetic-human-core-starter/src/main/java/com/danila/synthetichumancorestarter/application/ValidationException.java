package com.danila.synthetichumancorestarter.application;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
