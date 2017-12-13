#!/bin/bash
IMAGE_NAME="mongo:3.2"
CONTAINER_NAME_PREFIX=mongo-server

if [ ! "$(docker images ${IMAGE_NAME} | grep mongo)" ]; then
    docker pull ${IMAGE_NAME}
fi

start() {
    CONTAINER_NAME="${CONTAINER_NAME_PREFIX}-$1"
    MONGO_PORT=$2
    if [ ! "$(docker ps -q -f name=${CONTAINER_NAME})" ]; then
        if [ "$(docker ps -aq -f status=exited -f name=${CONTAINER_NAME})" ]; then
            echo "Cleaning up ${CONTAINER_NAME}"
            docker rm ${CONTAINER_NAME}
        fi
        echo "Running ${CONTAINER_NAME}"
        docker run -d -p ${MONGO_PORT}:27017 --name ${CONTAINER_NAME} ${IMAGE_NAME} --auth
        # Allow mongo time to start
        sleep 2
        echo "Creating admin user"
        docker exec -it ${CONTAINER_NAME} mongo admin --eval "db.createUser({user:'admin',pwd:'Passw0rd',roles:['userAdminAnyDatabase']})"
    else
        echo "${CONTAINER_NAME} is already running."
    fi
}

case "$1" in
    dev)
        start $1 27017
        ;;
    demo)
        start $1 27018
        ;;
    *)
        echo "Usage: $0 {dev|demo}"
        exit 1
esac