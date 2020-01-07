#!/bin/bash
BLUE='\033[0;34m'
NC='\033[0m' # No Color

printf "[${BLUE}INFO${NC}] Stopping and removing kibana container if it exists"
docker stop kibana || true && docker rm kibana || true
printf "[${BLUE}INFO${NC}] Stopping and removing elasticsearch container if it exists"
docker stop elasticsearch || true && docker rm elasticsearch || true
printf "[${BLUE}INFO${NC}] Stopping and removing postgres container if it exists"
docker stop postgres || true && docker rm postgres || true
printf "[${BLUE}INFO${NC}] Removing teamplanner network if it exists"
docker network rm teamplanner || true
printf "[${BLUE}INFO${NC}] Create teamplanner network"
docker network create teamplanner
printf "[${BLUE}INFO${NC}] Run and expose fresh postgres container id: "
docker run --name postgres --net teamplanner -e POSTGRES_DB=teamplanner -e POSTGRES_USER=tp_admin -e POSTGRES_PASSWORD=t3amp1anner -p 5432:5432 -d postgres:12.1-alpine
printf "[${BLUE}INFO${NC}] Run and expose fresh elasticsearch container id: "
docker run --name elasticsearch --net teamplanner -e discovery.type=single-node -p 9200:9200 -p 9300:9300  -d elasticsearch:7.5.1
printf "[${BLUE}INFO${NC}] Run and expose fresh kibana container id :"
docker run --name kibana --net teamplanner -e ELASTICSEARCH_HOSTS=http://elasticsearch:9200 -p 5601:5601 -d kibana:7.5.1
printf "[${BLUE}INFO${NC}] Start backend in hot replace development mode\n"
mvn quarkus:dev