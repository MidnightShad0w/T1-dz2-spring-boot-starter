package com.danila.synthetichumancorestarter.infrastructure.audit;

public class ConsoleAuditPublisher implements AuditPublisher{
    @Override
    public void send(AuditRecord record) {
        System.out.println("[AUDIT] " + record);
    }
}
