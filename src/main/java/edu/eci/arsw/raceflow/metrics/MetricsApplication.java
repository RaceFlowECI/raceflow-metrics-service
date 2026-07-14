package edu.eci.arsw.raceflow.metrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del esqueleto de Metrics Service. Reservado para consumir eventos
 * de dominio desde RabbitMQ y calcular agregaciones de KPI; se mantiene como un
 * microservicio placeholder con su andamiaje de métricas en su lugar.
 */
@SpringBootApplication
public class MetricsApplication {
    /** @param args argumentos de línea de comandos pasados a Spring Boot */
    public static void main(String[] args) {
        SpringApplication.run(MetricsApplication.class, args);
    }
}
