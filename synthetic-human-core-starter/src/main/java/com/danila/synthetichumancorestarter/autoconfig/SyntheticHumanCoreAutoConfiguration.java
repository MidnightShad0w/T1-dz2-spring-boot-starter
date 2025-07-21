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
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.concurrent.*;

@AutoConfiguration
@EnableScheduling
@ComponentScan
@EnableConfigurationProperties({QueueProperties.class, AuditProperties.class, QueueMonitorProperties.class})
public class SyntheticHumanCoreAutoConfiguration {
    @Bean("commonQueue")
    CommandQueue commonQueue(QueueProperties p) {
        return new CommandQueue("COMMON", p.commonThreads(), p.commonCapacity());
    }

    @Bean("criticalQueue")
    CommandQueue criticalQueue(QueueProperties p) {
        return new CommandQueue("CRITICAL", p.criticalThreads(), p.criticalCapacity());
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
    CommandDispatcher dispatcher(@Qualifier("commonQueue") CommandQueue commonQ,
                                 @Qualifier("criticalQueue") CommandQueue criticalQ,
                                 MetricsService metrics) {
        return new CommandDispatcher(commonQ, criticalQ, metrics);
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
            ObjectProvider<KafkaTemplate<String, String>> kafkaProvider) {

        if (props.getMode() == AuditProperties.Mode.KAFKA) {
            return new KafkaAuditPublisher(
                    kafkaProvider.getIfAvailable(),
                    props.getTopic());
        }
        return new ConsoleAuditPublisher();
    }

    @Bean
    WeylandWatchingYouAspect watchingAspect(AuditPublisher p) {
        return new WeylandWatchingYouAspect(p);
    }

    // metrics
    @Bean
    MetricsService metricsService(MeterRegistry reg, List<QueueReader> queues) {
        return new MetricsService(reg, queues);
    }

    @Bean
    QueueMonitor queueMonitor(List<QueueReader> queues,
                              MetricsService metrics,
                              QueueMonitorProperties props) {
        return new QueueMonitor(queues, metrics, props);
    }

}
