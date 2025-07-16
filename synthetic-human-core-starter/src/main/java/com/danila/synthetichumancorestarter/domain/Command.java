package com.danila.synthetichumancorestarter.domain;

import java.time.Instant;
import java.util.Objects;

public record Command(String description,
                      String author,
                      Priority priority,
                      Instant createdAt) {
    public Command {
        Objects.requireNonNull(description);
        Objects.requireNonNull(author);
        Objects.requireNonNull(priority);
        Objects.requireNonNull(createdAt);
    }

    public enum Priority { COMMON, CRITICAL }
}
