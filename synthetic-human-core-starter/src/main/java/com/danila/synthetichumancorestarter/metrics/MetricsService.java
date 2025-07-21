package com.danila.synthetichumancorestarter.metrics;

import com.danila.synthetichumancorestarter.application.QueueReader;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class MetricsService {

    private final MeterRegistry registry;
    private final ConcurrentMap<String, Counter> perAuthor = new ConcurrentHashMap<>();

    public MetricsService(MeterRegistry registry, List<QueueReader> queues) {
        this.registry = registry;
        queues.forEach(q ->
                Gauge.builder("command_queue_size", q::size)
                        .description("Commands in queue")
                        .tag("queue", q.name())
                        .register(registry));
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
