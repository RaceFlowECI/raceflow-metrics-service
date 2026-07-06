# RACEFLOW — Metrics Service

> [!IMPORTANT]
> Este repositorio contiene el **Metrics Service** de RaceFlow: metricas y rankings con patron CQRS.

> Para informacion general consulta el [perfil de la organizacion](https://github.com/RaceFlowECI).

---

## Tabla de contenido
- [Descripcion general](#descripcion-general)
- [Stack tecnologico](#stack-tecnologico)
- [Patron CQRS](#patron-cqrs)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Configuracion local](#configuracion-local)
- [Endpoints REST](#endpoints-rest)
- [Pruebas y calidad](#pruebas-y-calidad)
- [CI/CD](#cicd)

---

## Descripcion general

> [!NOTE]
> Microservicio de metricas y estadisticas deportivas. Implementa el patron CQRS: consume los eventos de sesion de RabbitMQ para actualizar el modelo de escritura (Commands) y expone endpoints de consulta optimizados para lectura (Queries).

### Responsabilidades principales

| Responsabilidad | Descripcion |
|---|---|
| **Ingesta** | Consume eventos de SessionFinished de RabbitMQ y persiste los datos. |
| **Consulta** | Expone estadisticas por usuario, por sala y globales. |
| **Rankings** | Calcula rankings historicos y por deporte. |
| **CQRS** | Separa el modelo de escritura (eventos) del modelo de lectura (queries). |

---

## Stack tecnologico

### Backend
![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

### Testing y calidad
![JUnit](https://img.shields.io/badge/JUnit_5-25A162?style=for-the-badge&logo=java&logoColor=white)
![JaCoCo](https://img.shields.io/badge/JaCoCo-BB0A30?style=for-the-badge)
![SonarQube](https://img.shields.io/badge/SonarQube-4E9BCD?style=for-the-badge&logo=sonarqube&logoColor=white)

### DevOps
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

---

## Patron CQRS

El servicio aplica **Command Query Responsibility Segregation** para separar la escritura de la lectura:

```
RabbitMQ → [Command Side]          [Query Side] → REST API
             SessionEventHandler      MetricsQueryService
             MetricsWriteService      MetricsReadRepository
             MetricsWriteRepository
```

- **Command Side**: escucha `session.finished` de RabbitMQ, persiste metricas en la tabla de escritura.
- **Query Side**: sirve consultas REST desde vistas o proyecciones optimizadas para lectura.

---

## Estructura del proyecto

```text
raceflow-metrics-service/
├── .github/workflows/
├── .env.example
├── .gitignore
├── Dockerfile
├── pom.xml
└── src/main/java/edu/eci/arsw/raceflow/metrics/
    ├── MetricsApplication.java
    ├── command/
    │   ├── SessionEventHandler.java
    │   └── MetricsWriteService.java
    ├── query/
    │   ├── MetricsQueryService.java
    │   └── MetricsReadRepository.java
    ├── model/
    │   └── Metric.java
    └── controller/
        └── MetricsController.java
```

---

## Configuracion local

### 1. Clonar el repositorio
```bash
git clone https://github.com/RaceFlowECI/raceflow-metrics-service.git
cd raceflow-metrics-service
```

### 2. Compilar
```bash
mvn clean install
```

### 3. Configurar variables de entorno
```bash
cp .env.example .env
```
```env
DB_HOST=localhost
DB_USER=raceflow
DB_PASSWORD=secret
RABBITMQ_HOST=localhost
```

### 4. Ejecutar
```bash
mvn spring-boot:run
```
> [!TIP]
> El servicio arranca en `http://localhost:8085`. Requiere PostgreSQL y RabbitMQ.

---

## Endpoints REST

| Metodo | Ruta | Auth | Descripcion |
|---|---|---|---|
| `GET` | `/metrics/user/{userId}` | JWT | Estadisticas historicas de un usuario. |
| `GET` | `/metrics/room/{roomCode}` | JWT | Metricas de una sala especifica. |
| `GET` | `/metrics/rankings` | JWT | Ranking global de participantes. |
| `GET` | `/metrics/rankings/{sport}` | JWT | Ranking por deporte (RUNNING, CYCLING, SWIMMING). |

### Ejemplo: obtener ranking global
```bash
curl http://localhost:8085/metrics/rankings \
  -H "Authorization: Bearer <token>"
```

---

## Pruebas y calidad
```bash
mvn test
mvn clean test jacoco:report
```

---

## CI/CD

| Campo | Valor |
|---|---|
| Puerto | 8085 |
| Plataforma | _por definir_ |
| Ultima version | ![CI](https://github.com/RaceFlowECI/raceflow-metrics-service/actions/workflows/ci.yml/badge.svg) |
