package edu.eci.arsw.raceflow.metrics.messaging;

import edu.eci.arsw.raceflow.metrics.metrics.MetricsServiceMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes room lifecycle events from the {@code metrics.room-events} queue
 * (bound to {@code room.*} on the shared {@code raceflow.events} exchange,
 * see {@link edu.eci.arsw.raceflow.metrics.config.RabbitMQConfig}) and
 * records them via {@link MetricsServiceMetrics}.
 */
@Component
public class RoomEventListener {

    private static final Logger log = LoggerFactory.getLogger(RoomEventListener.class);

    private final MetricsServiceMetrics metrics;
    private final MeterRegistry registry;

    public RoomEventListener(MetricsServiceMetrics metrics, MeterRegistry registry) {
        this.metrics = metrics;
        this.registry = registry;
    }

    /** @param event the received room lifecycle event, deserialized from JSON */
    @RabbitListener(queues = "metrics.room-events")
    public void onRoomEvent(RoomEvent event) {
        log.info("Consumed event {} for room {} (created by {})",
                event.getEventType(), event.getRoomCode(), event.getCreatedBy());
        metrics.recordEventConsumed(event.getEventType(), registry);
    }
}
