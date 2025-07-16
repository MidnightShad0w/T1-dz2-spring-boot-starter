package com.danila.synthetichumancorestarter.application;

import com.danila.synthetichumancorestarter.domain.Command;
import com.danila.synthetichumancorestarter.domain.Command.Priority;
import com.danila.synthetichumancorestarter.metrics.MetricsService;

import java.util.concurrent.ExecutorService;

public class CommandDispatcher {
    private final CommandQueue queue;
    private final ExecutorService criticalExecutor;
    private final MetricsService metrics;

    public CommandDispatcher(CommandQueue queue,
                             ExecutorService criticalExecutor,
                             MetricsService metrics) {
        this.queue = queue;
        this.criticalExecutor = criticalExecutor;
        this.metrics = metrics;
    }

    public void dispatch(Command cmd) {
        if (cmd.priority() == Priority.CRITICAL) {
            criticalExecutor.submit(() -> execute(cmd));
        } else {
            queue.enqueue(cmd);
        }
    }

    private void execute(Command cmd) {
        try {
            Thread.sleep(20000);
            metrics.incrementCompleted(cmd.author());
        } catch (Exception ex) {
            // metrics.incrementFailed(cmd.author())
        }
    }
}
