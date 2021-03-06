#!/bin/bash
IMAGE_NAME="spotify/kafka"
CONTAINER_NAME=kafka-server

if [ ! "$(docker images ${IMAGE_NAME} | grep kafka)" ]; then
    docker pull ${IMAGE_NAME}
fi

HOSTIP=`ip addr show | grep docker0 | grep global | awk '{print $2}' | cut -d / -f1`

if [ ! "$(docker ps -q -f name=${CONTAINER_NAME})" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=${CONTAINER_NAME})" ]; then
        echo "Cleaning up ${CONTAINER_NAME}"
        docker rm ${CONTAINER_NAME}
    fi
    echo "Running ${CONTAINER_NAME}"
    docker run -d --name ${CONTAINER_NAME} -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=$HOSTIP --env ADVERTISED_PORT=9092 ${IMAGE_NAME}
else
    echo "${CONTAINER_NAME} is already running."
fi
