package edu.eci.arsw.raceflow.metrics.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declares this service's queue against the shared {@code raceflow.events}
 * topic exchange (also declared by publishers, e.g. realtime-service) and
 * binds it to every {@code room.*} routing key.
 */
@Configuration
public class RabbitMQConfig {

    private static final String EVENTS_EXCHANGE = "raceflow.events";
    private static final String ROOM_EVENTS_QUEUE = "metrics.room-events";

    /** @return the shared topic exchange, durable so it survives a broker restart */
    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EVENTS_EXCHANGE, true, false);
    }

    /** @return this service's durable queue for room lifecycle events */
    @Bean
    public Queue roomEventsQueue() {
        return new Queue(ROOM_EVENTS_QUEUE, true);
    }

    /** @return the binding routing every {@code room.*} event into {@link #roomEventsQueue()} */
    @Bean
    public Binding roomEventsBinding(Queue roomEventsQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(roomEventsQueue).to(eventsExchange).with("room.*");
    }

    /** @return a JSON message converter so event payloads deserialize into DTOs, not raw bytes */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
