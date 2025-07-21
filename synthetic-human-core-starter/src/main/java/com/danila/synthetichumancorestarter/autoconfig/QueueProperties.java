package com.danila.synthetichumancorestarter.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "synth.queue")
public record QueueProperties(int capacity, @DefaultValue("1") int commonThreads, @DefaultValue("1") int criticalThreads) {
}
