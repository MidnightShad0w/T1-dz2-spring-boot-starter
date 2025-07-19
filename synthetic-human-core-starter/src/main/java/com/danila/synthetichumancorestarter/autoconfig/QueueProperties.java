package com.danila.synthetichumancorestarter.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "synth.queue")
public record QueueProperties(int capacity, int criticalThreads) {
}
