package edu.eci.arsw.raceflow.metrics.messaging;

import edu.eci.arsw.raceflow.metrics.metrics.MetricsServiceMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class RoomEventListenerTest {

    @Test
    void recordsTheConsumedEventByType() {
        MeterRegistry registry = new SimpleMeterRegistry();
        MetricsServiceMetrics metrics = new MetricsServiceMetrics(registry);
        RoomEventListener listener = new RoomEventListener(metrics, registry);

        double before = registry.counter("raceflow.events.consumed", "event_type", "room.activated").count();

        RoomEvent event = new RoomEvent();
        event.setEventType("room.activated");
        event.setRoomCode("ABC123");
        event.setCreatedBy("juan@raceflow.dev");
        event.setTimestamp(Instant.now());

        listener.onRoomEvent(event);

        double after = registry.counter("raceflow.events.consumed", "event_type", "room.activated").count();
        assertThat(after).isEqualTo(before + 1);
    }
}
