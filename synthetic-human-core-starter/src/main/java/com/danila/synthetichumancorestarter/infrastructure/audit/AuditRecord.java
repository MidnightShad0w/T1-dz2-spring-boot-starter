package com.danila.synthetichumancorestarter.infrastructure.audit;

public record AuditRecord(String method, String arguments, String result) {
}
