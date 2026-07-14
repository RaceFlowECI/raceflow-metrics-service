package edu.eci.arsw.raceflow.metrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Metrics Service skeleton. Reserved for consuming domain
 * events from RabbitMQ and computing KPI aggregations; kept as a placeholder
 * microservice with its metrics scaffolding in place.
 */
@SpringBootApplication
public class MetricsApplication {
    /** @param args command-line arguments passed to Spring Boot */
    public static void main(String[] args) {
        SpringApplication.run(MetricsApplication.class, args);
    }
}
