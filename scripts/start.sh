#!/usr/bin/env bash

PROJECT_ROOT="/home/ec2-user/app"
DOCKER_COMPOSE_FILE="$PROJECT_ROOT/prod.docker-compose.yml"

docker-compose -f $DOCKER_COMPOSE_FILE down && docker-compose -f $DOCKER_COMPOSE_FILE up --build
