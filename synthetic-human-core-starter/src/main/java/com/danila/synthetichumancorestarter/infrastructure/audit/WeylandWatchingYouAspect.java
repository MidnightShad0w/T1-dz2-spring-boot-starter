package com.danila.synthetichumancorestarter.infrastructure.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StringUtils;

@Aspect
public class WeylandWatchingYouAspect {
    private final AuditPublisher publisher;
    public WeylandWatchingYouAspect(AuditPublisher publisher) { this.publisher = publisher; }

    @Around("@annotation(com.danila.synthetichumancorestarter.infrastructure.audit.WeylandWatchingYou)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        String args = StringUtils.arrayToCommaDelimitedString(pjp.getArgs());
        publisher.send(new AuditRecord(pjp.getSignature().toShortString(), args, String.valueOf(result)));
        return result;
    }
}
