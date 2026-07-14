package edu.eci.arsw.raceflow.metrics.metrics;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

/** Micrometer counters/timers for metrics-service itself, exposed at {@code /actuator/prometheus}. */
@Component
public class MetricsServiceMetrics {

    private final Counter eventsConsumed;
    private final Counter consumptionLag;
    private final Timer kpiComputationDuration;

    /** @param registry the Micrometer registry to bind these meters to */
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

    /**
     * Increments the events-consumed counter, tagged by event type.
     *
     * @param eventType one of {@code session.finished}, {@code room.activated}, {@code room.finished}
     * @param registry  the registry to resolve the tagged counter from
     */
    public void recordEventConsumed(String eventType, MeterRegistry registry) {
        registry.counter("raceflow.events.consumed", "event_type", eventType).increment();
    }
    /** Increments the counter of events lagging behind in the consumption queue. */
    public void recordConsumptionLag() { consumptionLag.increment(); }
    /** @return the timer used to measure KPI aggregation computation duration */
    public Timer getKpiComputationDuration() { return kpiComputationDuration; }
}
