package com.danila.synthetichumancorestarter.application;

import com.danila.synthetichumancorestarter.domain.Command;

import java.time.Instant;
import java.util.regex.Pattern;

public class CommandValidator {
    public void validate(Command cmd) {
        if (cmd.description() == null || cmd.description().isBlank())
            throw new ValidationException("Description is empty");

        if (cmd.author() == null || cmd.author().length() > 100)
            throw new ValidationException("Author length > 100");

        if (!AUTHOR_PATTERN.matcher(cmd.author()).matches())
            throw new ValidationException("Author contains invalid chars");

        if (cmd.createdAt().isAfter(Instant.now()))
            throw new ValidationException("createdAt is in the future");
    }

    private static final Pattern AUTHOR_PATTERN =
            Pattern.compile("^[\\p{L}0-9 _.-]+$");
}
