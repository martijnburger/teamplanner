# Teamplanner backend

REST API for Teamplanner, built with [Quarkus](https://quarkus.io/) on Java 21. Data is stored in PostgreSQL and searched through Elasticsearch.

## Running locally

Requirements: Java 21 and a running Docker (or Podman).

```shell
./mvnw quarkus:dev
```

Quarkus Dev Services starts PostgreSQL and Elasticsearch containers automatically and loads the sample data from `src/main/resources/import.sql`. The API is available at http://localhost:8080/api/v1.0/ and the Swagger UI at http://localhost:8080/q/swagger-ui.

## Building

```shell
./mvnw package
docker build -f src/main/docker/Dockerfile.jvm -t teamplanner/backend-jvm .
```

For a native executable, run `./mvnw package -Dnative` and use `src/main/docker/Dockerfile.native-micro`.

## Configuration

In production the application reads its connection settings from the environment:

| Variable | Description |
| --- | --- |
| `TEAMPLANNER_DB_URL` | JDBC URL, e.g. `jdbc:postgresql://db:5432/teamplanner` |
| `TEAMPLANNER_DB_USER` | Database user |
| `TEAMPLANNER_DB_PASSWORD` | Database password |
| `TEAMPLANNER_ELASTICSEARCH_HOSTS` | Elasticsearch host(s), e.g. `elasticsearch:9200` |
