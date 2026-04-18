# RIU Backend - Hotel Availability Search API

## Descripción

API REST desarrollada con **Spring Boot 3.5.13** y **Java 21** que permite buscar disponibilidad hotelera y contar búsquedas idénticas. Utiliza **Apache Kafka** como sistema de mensajería y **PostgreSQL** como base de datos.

### Arquitectura

El proyecto sigue una **arquitectura hexagonal** (Ports & Adapters) sin módulos:

```
src/main/java/com/.../riubackend/
├── domain/                          # Capa de dominio (modelos, puertos, excepciones)
│   ├── model/                       # HotelSearch, SearchCount (records inmutables)
│   ├── port/
│   │   ├── in/                      # Puertos de entrada (use cases)
│   │   └── out/                     # Puertos de salida (repository, event publisher)
│   └── exception/                   # Excepciones de dominio
├── application/                     # Capa de aplicación (servicios)
│   └── service/                     # CreateSearchService, GetSearchCountService, PersistSearchService
└── infrastructure/                  # Capa de infraestructura (adaptadores)
    ├── adapter/
    │   ├── in/
    │   │   ├── rest/                # Controlador REST, DTOs, validadores, mapper
    │   │   └── kafka/               # Consumidor Kafka
    │   └── out/
    │       ├── kafka/               # Productor Kafka, DTOs, mapper
    │       └── persistence/         # Entidades JPA, repositorio, mapper
    └── config/                      # Configuración Kafka, OpenAPI
```

### Tecnologías

- Java 21 (con hilos virtuales)
- Spring Boot 3.5.13
- Spring Kafka
- Spring Data JPA
- PostgreSQL 16
- Apache Kafka (Confluent 7.7.1)
- Lombok & MapStruct
- SpringDoc OpenAPI (Swagger UI)
- JaCoCo (cobertura de tests ≥ 80%)
- Docker & Docker Compose

---

## Requisitos previos

Solo se necesita tener instalado:

- **Docker** y **Docker Compose**

No se requiere Java, Maven ni ninguna otra herramienta. La compilación se realiza dentro del contenedor Docker.

---

## Cómo levantar la aplicación

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd riubackend
```

### 2. Construir y levantar con Docker Compose

```bash
docker-compose up --build -d
```

Este comando:
- Levanta **PostgreSQL** en el puerto `5432`
- Levanta **Zookeeper** en el puerto `2181`
- Levanta **Kafka** en el puerto `9092`
- Levanta **kafka-ui** en el puerto `9090`
- Compila la aplicación Java dentro de Docker (multi-stage build)
- Levanta la **aplicación** en el puerto `8080`

### 3. Verificar que todo está corriendo

```bash
docker-compose ps
```

### 4. Detener la aplicación

```bash
docker-compose down
```

Para eliminar también los volúmenes de datos:

```bash
docker-compose down -v
```
---

## Swagger / OpenAPI

Una vez la aplicación esté levantada, se puede acceder a la documentación interactiva de la API:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

---

## Endpoints

### POST `/search`

Crea una nueva búsqueda de disponibilidad hotelera.

**Request Body:**
```json
{
    "hotelId": "1234aBc",
    "checkIn": "29/12/2023",
    "checkOut": "31/12/2023",
    "ages": [30, 29, 1, 3]
}
```

**Response (200 OK):**
```json
{
    "searchId": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Validaciones:**
- `hotelId`: no puede ser nulo ni vacío
- `checkIn`: no puede ser nulo, debe ser anterior a `checkOut`
- `checkOut`: no puede ser nulo
- `ages`: no puede ser nulo ni vacío, todas las edades deben ser ≥ 0

### GET `/count?searchId=xxx`

Obtiene el número de búsquedas idénticas para un `searchId` dado.

**Response (200 OK):**
```json
{
    "searchId": "550e8400-e29b-41d4-a716-446655440000",
    "search": {
        "hotelId": "1234aBc",
        "checkIn": "29/12/2023",
        "checkOut": "31/12/2023",
        "ages": [1, 3, 29, 30]
    },
    "count": 100
}
```

> **Nota:** Las edades en la respuesta se devuelven **ordenadas**. Está hecho así porque desde el punto de vista de negocio, una búsqueda de hotel con edades **[30, 29, 1, 3]** y otra con **[1, 3, 29, 30]** representan la misma búsqueda (mismas personas, mismo hotel, mismas fechas). Sería incorrecto contarlas como búsquedas distintas solo porque el cliente envió las edades en otro orden.

---

## Tests

Los tests se ejecutan automáticamente durante el build de Docker. Para ejecutarlos manualmente (requiere Java 21 y Maven):

```bash
./mvnw clean verify
```

El reporte de cobertura JaCoCo se genera en `target/site/jacoco/index.html`.

---

## Notas técnicas

- **Inmutabilidad**: Todos los DTOs y modelos de dominio son `record` de Java con copias defensivas.
- **Thread safety**: Las peticiones son thread-safe gracias al uso de objetos inmutables y Spring beans singleton sin estado mutable.
- **Hilos virtuales**: La persistencia en base de datos desde el consumidor Kafka se realiza con hilos virtuales de Java 21.
- **SQL Injection**: Prevenida mediante el uso de JPA con queries parametrizadas (nunca se concatenan strings en queries).
- **Principios SOLID**: Interfaces segregadas para cada use case, dependencias invertidas mediante puertos y adaptadores.

