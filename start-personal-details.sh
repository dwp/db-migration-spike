#!/bin/bash

# Start Mongo if not running
if [ ! "$(docker ps -q -f name=mongo-server-demo)" ]; then
    `dirname $0`/start-mongo.sh demo
fi

# Confirm Kafka is running
if [ ! "$(docker ps -q -f name=kafka-server)" ]; then
    `dirname $0`/start-kafka.sh
fi

echo "Bootstrap Mongo..."
export MONGO_DB_ADDRESS="localhost:27018/personal-details"
./src/uk/gov/dwp/personal/details/server/dao/mongo/mongo-personal-details-roles.sh
./src/uk/gov/dwp/personal/details/server/dao/mongo/mongo-personal-details-users.sh

IMAGE_NAME="personal-details-server:latest"
IMAGE_PATH="buck-out/gen/src/uk/gov/dwp/personal/details/server/docker-build/personal-details-server-latest.docker"
CONTAINER_NAME="personal-details-server-latest"

# TODO: Consider adding docker build as a pre-step

if [ ! "$(docker ps -q -f name=${CONTAINER_NAME})" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=${CONTAINER_NAME})" ]; then
        echo "Cleaning up ${CONTAINER_NAME}"
        docker rm ${CONTAINER_NAME}
    fi
    docker load --input ${IMAGE_PATH}
    echo "Running ${CONTAINER_NAME}"
    docker run --name ${CONTAINER_NAME} --link mongo-server-demo --link kafka-server -it -p 9009:8008 -e SPRING_PROFILES_ACTIVE='docker' ${IMAGE_NAME}
else
    echo "${CONTAINER_NAME} is already running."
fi
