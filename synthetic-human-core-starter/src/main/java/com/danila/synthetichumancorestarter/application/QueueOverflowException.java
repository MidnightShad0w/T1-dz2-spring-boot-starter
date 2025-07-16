package com.danila.synthetichumancorestarter.application;

public class QueueOverflowException extends RuntimeException {
    public QueueOverflowException(String msg) {
        super(msg);
    }
}
