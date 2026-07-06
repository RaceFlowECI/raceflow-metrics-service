package edu.eci.arsw.raceflow.metrics.metrics;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MetricsServiceMetrics {

    private final Counter eventsConsumed;
    private final Counter consumptionLag;
    private final Timer kpiComputationDuration;

    public MetricsServiceMetrics(MeterRegistry registry) {
        this.eventsConsumed = Counter.builder("raceflow.events.consumed")
                .description("Total events consumed from RabbitMQ")
                .tag("event_type", "unknown")
                .register(registry);

        this.consumptionLag = Counter.builder("raceflow.events.consumption.lag")
                .description("Events lagging behind in the consumption queue")
                .register(registry);

        this.kpiComputationDuration = Timer.builder("raceflow.kpi.computation.duration")
                .description("Time to compute KPI aggregations")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);

        // Pre-register known event types
        Counter.builder("raceflow.events.consumed").tag("event_type", "session.finished").register(registry);
        Counter.builder("raceflow.events.consumed").tag("event_type", "room.activated").register(registry);
        Counter.builder("raceflow.events.consumed").tag("event_type", "room.finished").register(registry);
    }

    public void recordEventConsumed(String eventType, MeterRegistry registry) {
        registry.counter("raceflow.events.consumed", "event_type", eventType).increment();
    }
    public void recordConsumptionLag() { consumptionLag.increment(); }
    public Timer getKpiComputationDuration() { return kpiComputationDuration; }
}
