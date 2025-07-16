package com.danila.synthetichumancorestarter.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("synth.audit")
public class AuditProperties {

    public enum Mode { CONSOLE, KAFKA }

    private Mode mode = Mode.CONSOLE;
    private String topic = "synth.audit.commands";

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
