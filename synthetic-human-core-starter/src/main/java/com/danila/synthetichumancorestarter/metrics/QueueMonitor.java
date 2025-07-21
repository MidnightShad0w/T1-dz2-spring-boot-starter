package com.danila.synthetichumancorestarter.metrics;

import com.danila.synthetichumancorestarter.application.QueueReader;
import com.danila.synthetichumancorestarter.autoconfig.QueueMonitorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class QueueMonitor {
    private final List<QueueReader> queues;
    private final MetricsService metrics;
    private final QueueMonitorProperties props;

    private static final Logger log = LoggerFactory.getLogger(QueueMonitor.class);

    public QueueMonitor(List<QueueReader> queues, MetricsService metrics, QueueMonitorProperties props) {
        this.queues = queues;
        this.metrics = metrics;
        this.props = props;
    }

    @Scheduled(
            fixedDelayString =
                    "#{T(java.time.Duration).parse('${synth.monitor.interval:PT10S}').toMillis()}"
    )
    public void report() {
        queues.forEach(q ->
                log.info("MONITOR {} size={}", q.name(), q.size()));
        log.info("completed={}", metrics.snapshot());
    }
}
