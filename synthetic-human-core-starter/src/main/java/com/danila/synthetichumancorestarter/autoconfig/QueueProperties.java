package com.danila.synthetichumancorestarter.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("synthetic.queue")
public record QueueProperties(int capacity, int criticalThreads) {
}
