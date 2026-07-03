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

---

## Patron CQRS

---

## Estructura del proyecto

---

## Configuracion local

---

## Endpoints REST

---

## Pruebas y calidad

---

## CI/CD

