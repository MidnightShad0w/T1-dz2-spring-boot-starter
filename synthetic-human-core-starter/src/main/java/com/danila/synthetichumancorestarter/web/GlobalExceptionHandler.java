package com.danila.synthetichumancorestarter.web;

import com.danila.synthetichumancorestarter.application.QueueOverflowException;
import com.danila.synthetichumancorestarter.application.ValidationException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> bad(ValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, e.getMessage(), null));
    }

    @ExceptionHandler(QueueOverflowException.class)
    public ResponseEntity<ApiError> tooMany(QueueOverflowException e) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(ApiError.of(429, e.getMessage(), null));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> unreadable(HttpMessageNotReadableException ex) {
        String msg = "request body is malformed";

        // InvalidFormatException означает, что Jackson не смог
        // преобразовать строку в нужный тип (в нашем случае enum Priority)
        if (ex.getCause() instanceof InvalidFormatException ife) {
            if (ife.getTargetType().isEnum()
                    && ife.getTargetType().getSimpleName().equals("Priority")) {
                msg = "priority is required (COMMON|CRITICAL)";
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, msg, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> fail(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(500, "Internal error", e.getMessage()));
    }
}
