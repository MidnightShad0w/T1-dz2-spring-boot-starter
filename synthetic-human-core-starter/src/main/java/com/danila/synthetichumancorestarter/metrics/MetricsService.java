package com.danila.synthetichumancorestarter.metrics;

import com.danila.synthetichumancorestarter.application.CommandQueue;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MetricsService {
    private final MeterRegistry registry;
    private final ConcurrentMap<String, Counter> perAuthorCounters = new ConcurrentHashMap<>();

    public MetricsService(MeterRegistry registry, CommandQueue queue) {
        this.registry = registry;
        Gauge.builder("command_queue_size", queue::size)
                .description("Current number of commands in queue")
                .register(registry);
    }

    public void incrementCompleted(String author) {
        perAuthorCounters
                .computeIfAbsent(author,
                        a -> Counter.builder("command_completed_total")
                                .description("Finished commands by author")
                                .tag("author", a)
                                .register(registry))
                .increment();
    }
}
