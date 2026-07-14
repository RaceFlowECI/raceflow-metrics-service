package edu.eci.arsw.raceflow.metrics.metrics;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

/** Contadores/temporizadores de Micrometer para el propio metrics-service, expuestos en {@code /actuator/prometheus}. */
@Component
public class MetricsServiceMetrics {

    private final Counter eventsConsumed;
    private final Counter consumptionLag;
    private final Timer kpiComputationDuration;

    /** @param registry el registro de Micrometer al cual asociar estas métricas */
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
     * Incrementa el contador de eventos consumidos, etiquetado por tipo de evento.
     *
     * @param eventType uno de {@code session.finished}, {@code room.activated}, {@code room.finished}
     * @param registry  el registro del cual resolver el contador etiquetado
     */
    public void recordEventConsumed(String eventType, MeterRegistry registry) {
        registry.counter("raceflow.events.consumed", "event_type", eventType).increment();
    }
    /** Incrementa el contador de eventos rezagados en la cola de consumo. */
    public void recordConsumptionLag() { consumptionLag.increment(); }
    /** @return el temporizador usado para medir la duración del cálculo de agregaciones de KPI */
    public Timer getKpiComputationDuration() { return kpiComputationDuration; }
}
