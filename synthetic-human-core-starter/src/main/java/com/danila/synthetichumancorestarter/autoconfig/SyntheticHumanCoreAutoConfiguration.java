package com.danila.synthetichumancorestarter.autoconfig;

import com.danila.synthetichumancorestarter.application.CommandDispatcher;
import com.danila.synthetichumancorestarter.application.CommandQueue;
import com.danila.synthetichumancorestarter.application.CommandService;
import com.danila.synthetichumancorestarter.application.CommandValidator;
import com.danila.synthetichumancorestarter.infrastructure.audit.AuditPublisher;
import com.danila.synthetichumancorestarter.infrastructure.audit.ConsoleAuditPublisher;
import com.danila.synthetichumancorestarter.infrastructure.audit.KafkaAuditPublisher;
import com.danila.synthetichumancorestarter.infrastructure.audit.WeylandWatchingYouAspect;
import com.danila.synthetichumancorestarter.metrics.MetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AutoConfiguration
@EnableConfigurationProperties({QueueProperties.class, AuditProperties.class})
public class SyntheticHumanCoreAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    CommandQueue commandQueue(QueueProperties props) {
        return new CommandQueue(props.capacity());
    }

    @Bean
    ExecutorService criticalExecutor(QueueProperties props) {
        return Executors.newFixedThreadPool(props.criticalThreads());
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
    @ConditionalOnMissingBean
    CommandDispatcher dispatcher(CommandQueue q,
                                 ExecutorService ex,
                                 MetricsService metrics) {
        return new CommandDispatcher(q, ex, metrics);
    }
}
