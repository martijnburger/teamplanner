#!/bin/bash
docker stop kibana || true && docker rm kibana || true
docker stop elasticsearch || true && docker rm elasticsearch || true
docker stop postgres || true && docker rm postgres || true
docker network rm teamplanner || true
docker network create teamplanner
docker run --name postgres --net teamplanner -e POSTGRES_DB=teamplanner -e POSTGRES_USER=tp_admin -e POSTGRES_PASSWORD=t3amp1anner -p 5432:5432 -d postgres:12.1-alpine
docker run --name elasticsearch --net teamplanner -e discovery.type=single-node -p 9200:9200 -p 9300:9300  -d elasticsearch:7.5.1
docker run --name kibana --net teamplanner -e ELASTICSEARCH_HOSTS=http://elasticsearch:9200 -p 5601:5601 -d kibana:7.5.1
cd backend
mvn quarkus:dev