package com.danila.synthetichumancorestarter.metrics;

import com.danila.synthetichumancorestarter.application.CommandQueue;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class MetricsService {

    private final MeterRegistry registry;
    private final ConcurrentMap<String, Counter> perAuthor = new ConcurrentHashMap<>();

    public MetricsService(MeterRegistry registry, CommandQueue queue) {
        this.registry = registry;
        Gauge.builder("command_queue_size", queue::size)
                .description("Current number of commands in queue")
                .register(registry);
    }

    public void incrementCompleted(String author) {
        perAuthor
                .computeIfAbsent(author,
                        a -> Counter.builder("command_completed_total")
                                .tag("author", a)
                                .description("Finished commands by author")
                                .register(registry))
                .increment();
    }

    public Map<String, Double> snapshot() {
        return perAuthor.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().count()));
    }
}
