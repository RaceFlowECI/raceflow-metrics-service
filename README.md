# raceflow-metrics-service
Microservicio de observabilidad de RaceFlow. Calcula los 4 KPIs de negocio y métricas técnicas.
Construye su propio read model (patrón CQRS) consumiendo eventos de RabbitMQ — sin acceder a las bases de datos de los demás servicios.

**Stack:** Java 21 · Spring Boot · PostgreSQL (Metrics DB) · RabbitMQ (consumidor)
**KPIs:** Salas creadas · Atletas conectados · Sesiones finalizadas · Reacciones enviadas
**Escala:** ×1
