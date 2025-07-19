package com.danila.synthetichumancorestarter.application;

import com.danila.synthetichumancorestarter.domain.Command;
import com.danila.synthetichumancorestarter.domain.Command.Priority;
import com.danila.synthetichumancorestarter.metrics.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class CommandDispatcher {

    private final CommandQueue commonQueue;
    private final ExecutorService criticalExecutor;
    private final MetricsService metrics;

    private static final Logger log = LoggerFactory.getLogger(CommandDispatcher.class);

    public CommandDispatcher(CommandQueue commonQueue,
                             ExecutorService criticalExecutor,
                             MetricsService metrics) {
        this.commonQueue = commonQueue;
        this.criticalExecutor = criticalExecutor;
        this.metrics = metrics;
    }

    public void dispatch(Command cmd) {
        Runnable task = () -> execute(cmd);
        if (cmd.priority() == Priority.CRITICAL) {
            criticalExecutor.submit(task);
        } else {
            commonQueue.submit(task);
        }
    }

    private void execute(Command cmd) {
        try {
            log.info("ACCEPTED cmd={} priority={} createdAt={}", cmd.description(), cmd.priority(), cmd.createdAt());

            Thread.sleep(5000);
            metrics.incrementCompleted(cmd.author());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
