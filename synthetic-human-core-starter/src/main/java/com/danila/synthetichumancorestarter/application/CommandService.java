package com.danila.synthetichumancorestarter.application;

import com.danila.synthetichumancorestarter.domain.Command;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CommandService {
    private final CommandDispatcher dispatcher;
    private final CommandValidator validator;

    public CommandService(CommandDispatcher dispatcher, CommandValidator validator) {
        this.dispatcher = dispatcher;
        this.validator = validator;
    }

    public void accept(Command cmd) {
        validator.validate(cmd);
        dispatcher.dispatch(cmd);
    }
}
