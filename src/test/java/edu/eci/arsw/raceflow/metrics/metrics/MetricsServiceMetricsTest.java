package edu.eci.arsw.raceflow.metrics.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MetricsServiceMetricsTest {

    private MeterRegistry registry;
    private MetricsServiceMetrics metrics;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        metrics = new MetricsServiceMetrics(registry);
    }

    @Test
    void recordEventConsumedIncrementsCounterForGivenEventType() {
        metrics.recordEventConsumed("session.finished", registry);
        metrics.recordEventConsumed("session.finished", registry);

        assertThat(registry.get("raceflow.events.consumed")
                .tag("event_type", "session.finished")
                .counter().count())
                .isEqualTo(2.0);
    }

    @Test
    void recordEventConsumedRegistersNewEventTypeTag() {
        metrics.recordEventConsumed("custom.event", registry);

        assertThat(registry.get("raceflow.events.consumed")
                .tag("event_type", "custom.event")
                .counter().count())
                .isEqualTo(1.0);
    }

    @Test
    void recordConsumptionLagIncrementsCounter() {
        metrics.recordConsumptionLag();
        metrics.recordConsumptionLag();
        metrics.recordConsumptionLag();

        assertThat(registry.get("raceflow.events.consumption.lag").counter().count())
                .isEqualTo(3.0);
    }

    @Test
    void kpiComputationDurationTimerIsRegisteredAndRecordsTime() {
        metrics.getKpiComputationDuration().record(java.time.Duration.ofMillis(50));

        assertThat(registry.get("raceflow.kpi.computation.duration").timer().count())
                .isEqualTo(1);
    }

    @Test
    void preRegisteredEventTypeTagsAreAvailable() {
        assertThat(registry.get("raceflow.events.consumed")
                .tag("event_type", "room.activated")
                .counter().count())
                .isEqualTo(0.0);

        assertThat(registry.get("raceflow.events.consumed")
                .tag("event_type", "room.finished")
                .counter().count())
                .isEqualTo(0.0);
    }
}
