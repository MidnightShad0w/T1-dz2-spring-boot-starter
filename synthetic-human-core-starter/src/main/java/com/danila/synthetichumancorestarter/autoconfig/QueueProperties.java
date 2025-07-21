package com.danila.synthetichumancorestarter.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "synth.queue")
public record QueueProperties(int commonThreads,
                              int commonCapacity,
                              int criticalThreads,
                              int criticalCapacity) {
}
