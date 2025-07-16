package com.danila.bishopprototype.api.dto;

import com.danila.synthetichumancorestarter.domain.Command;
import com.danila.synthetichumancorestarter.domain.Command.Priority;

import java.time.Instant;

public record NewCommandDto(String description,
                            String author,
                            Priority priority) {
    public Command toCommand() {
        return new Command(description, author, priority, Instant.now());
    }
}
