package com.danila.synthetichumancorestarter.infrastructure.audit;

import org.springframework.kafka.core.KafkaTemplate;

public class KafkaAuditPublisher implements AuditPublisher {
    private final KafkaTemplate<String, String> kafka;
    private final String topic;

    public KafkaAuditPublisher(KafkaTemplate<String, String> kafka, String topic) {
        this.kafka = kafka;
        this.topic = topic;
    }

    @Override
    public void send(AuditRecord record) {
        kafka.send(topic, record.toString());
    }
}
