package com.danila.synthetichumancorestarter.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;

@ConfigurationProperties("synth.monitor")
public record QueueMonitorProperties(@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
                                     Duration interval) {
}
