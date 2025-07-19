package com.danila.synthetichumancorestarter.metrics;

import com.danila.synthetichumancorestarter.application.CommandQueue;
import com.danila.synthetichumancorestarter.autoconfig.QueueMonitorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class QueueMonitor {
    private final CommandQueue queue;
    private final MetricsService metrics;
    private final QueueMonitorProperties props;

    private static final Logger log = LoggerFactory.getLogger(QueueMonitor.class);

    public QueueMonitor(CommandQueue queue, MetricsService metrics, QueueMonitorProperties props) {
        this.queue = queue;
        this.metrics = metrics;
        this.props = props;
    }

    @Scheduled(
            fixedDelayString =
                    "#{T(java.time.Duration).parse('${synth.monitor.interval:PT10S}').toMillis()}"
    )
    public void report() {
        log.info("MONITOR queueSize={} completed={}",
                queue.size(),
                metrics.snapshot());
    }
}
