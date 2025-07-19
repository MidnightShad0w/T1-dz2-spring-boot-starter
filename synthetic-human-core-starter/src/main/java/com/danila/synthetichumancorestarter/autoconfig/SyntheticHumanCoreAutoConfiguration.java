package com.danila.synthetichumancorestarter.autoconfig;

import com.danila.synthetichumancorestarter.application.*;
import com.danila.synthetichumancorestarter.infrastructure.audit.AuditPublisher;
import com.danila.synthetichumancorestarter.infrastructure.audit.ConsoleAuditPublisher;
import com.danila.synthetichumancorestarter.infrastructure.audit.KafkaAuditPublisher;
import com.danila.synthetichumancorestarter.infrastructure.audit.WeylandWatchingYouAspect;
import com.danila.synthetichumancorestarter.metrics.MetricsService;
import com.danila.synthetichumancorestarter.metrics.QueueMonitor;
import com.danila.synthetichumancorestarter.web.GlobalExceptionHandler;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.*;

@AutoConfiguration
@EnableScheduling
@EnableConfigurationProperties({QueueProperties.class, AuditProperties.class, QueueMonitorProperties.class})
public class SyntheticHumanCoreAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    CommandQueue commandQueue(QueueProperties p) {
        return new CommandQueue(p.criticalThreads(), p.capacity());
    }

    @Bean
    ExecutorService criticalExecutor(QueueProperties props) {
        return new ThreadPoolExecutor(
                props.criticalThreads(), props.criticalThreads(),
                0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(),
                (r, executor) -> {
                    throw new QueueOverflowException("queue capacity exceeded");
                });
    }

    @Bean
    @ConditionalOnMissingBean
    CommandValidator commandValidator() {
        return new CommandValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    CommandService commandService(CommandDispatcher d, CommandValidator v) {
        return new CommandService(d, v);
    }

    @Bean
    @ConditionalOnMissingBean
    CommandDispatcher dispatcher(CommandQueue q,
                                 ExecutorService criticalEx,
                                 MetricsService metrics) {
        return new CommandDispatcher(q, criticalEx, metrics);
    }

    @Bean
    GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    // audit
    @Bean
    @ConditionalOnMissingBean
    AuditPublisher auditPublisher(
            AuditProperties props,
            org.springframework.beans.factory.ObjectProvider<org.springframework.kafka.core.KafkaTemplate<String, String>> kafkaProvider) {

        if (props.getMode() == AuditProperties.Mode.KAFKA) {
            var kt = kafkaProvider.getIfAvailable();
            return new KafkaAuditPublisher(kt, props.getTopic());
        }
        return new ConsoleAuditPublisher();
    }

    @Bean
    WeylandWatchingYouAspect watchingAspect(AuditPublisher p) {
        return new WeylandWatchingYouAspect(p);
    }

    // metrics
    @Bean
    MetricsService metricsService(MeterRegistry reg, CommandQueue queue) {
        return new MetricsService(reg, queue);
    }

    @Bean
    QueueMonitor queueMonitor(CommandQueue q,
                              MetricsService metrics,
                              QueueMonitorProperties props) {
        return new QueueMonitor(q, metrics, props);
    }

}
