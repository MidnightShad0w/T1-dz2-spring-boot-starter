package com.danila.synthetichumancorestarter.infrastructure.audit;

public interface AuditPublisher {
    void send(AuditRecord record);
}
