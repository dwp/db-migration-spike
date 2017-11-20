#!/bin/bash

echo "Bootstrap Mongo..."
export MONGO_DB_ADDRESS="localhost:28018/personal-details"
./src/uk/gov/dwp/personal/details/server/dao/mongo/mongo-personal-details-roles.sh
./src/uk/gov/dwp/personal/details/server/dao/mongo/mongo-personal-details-users.sh

#!/bin/bash
IMAGE_NAME="personal-details-server:1.0"
CONTAINER_NAME="personal-details-server-v1.0"

# TODO: Consider adding docker build as a pre-step

if [ ! "$(docker ps -q -f name=${CONTAINER_NAME})" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=${CONTAINER_NAME})" ]; then
        echo "Cleaning up ${CONTAINER_NAME}"
        docker rm ${CONTAINER_NAME}
    fi
    echo "Running ${CONTAINER_NAME}"
    docker run --name ${CONTAINER_NAME} --link mongo-server --link kafka-server -it -p 8008:8008 -e SPRING_PROFILES_ACTIVE='docker' ${IMAGE_NAME}
else
    echo "${CONTAINER_NAME} is already running."
fi
