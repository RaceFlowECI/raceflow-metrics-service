package edu.eci.arsw.raceflow.metrics.integration;

import edu.eci.arsw.raceflow.metrics.messaging.RoomEvent;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static java.time.Duration.ofSeconds;

/**
 * End-to-end integration test against a real RabbitMQ broker in Docker
 * (Testcontainers): publishes a {@code room.activated} event the same way
 * realtime-service does, and verifies this service's own listener consumes
 * it and increments the Micrometer counter. Overrides the
 * {@code spring.autoconfigure.exclude=RabbitAutoConfiguration} used by the
 * rest of the (fast, brokerless) test suite, since this test needs the real
 * thing.
 */
@Testcontainers
@SpringBootTest(properties = "spring.autoconfigure.exclude=")
class RoomEventRabbitIntegrationTest {

    @Container
    static RabbitMQContainer rabbitMQ = new RabbitMQContainer("rabbitmq:3-management-alpine");

    @DynamicPropertySource
    static void rabbitProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQ::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQ::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitMQ::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMQ::getAdminPassword);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MeterRegistry registry;

    @Test
    void publishedRoomActivatedEventIsConsumedAndRecorded() {
        double before = registry.counter("raceflow.events.consumed", "event_type", "room.activated").count();

        RoomEvent event = new RoomEvent();
        event.setEventType("room.activated");
        event.setRoomCode("XYZ789");
        event.setCreatedBy("ana@raceflow.dev");
        event.setTimestamp(Instant.now());

        rabbitTemplate.convertAndSend("raceflow.events", "room.activated", event);

        await().atMost(ofSeconds(10)).untilAsserted(() -> {
            double after = registry.counter("raceflow.events.consumed", "event_type", "room.activated").count();
            assertThat(after).isEqualTo(before + 1);
        });
    }
}
