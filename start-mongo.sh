#!/bin/bash
IMAGE_NAME="mongo:3.2"
CONTAINER_NAME=mongo-server

if [ ! "$(docker images ${IMAGE_NAME} | grep mongo)" ]; then
    docker pull ${IMAGE_NAME}
fi

if [ ! "$(docker ps -q -f name=${CONTAINER_NAME})" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=${CONTAINER_NAME})" ]; then
        echo "Cleaning up ${CONTAINER_NAME}"
        docker rm ${CONTAINER_NAME}
    fi
    echo "Running ${CONTAINER_NAME}"
    docker run -d -p 28018:27017 --name ${CONTAINER_NAME} ${IMAGE_NAME} --auth
    # Allow mongo time to start
    sleep 2
    echo "Creating admin user"
    docker exec -it ${CONTAINER_NAME} mongo admin --eval "db.createUser({user:'admin',pwd:'Passw0rd',roles:['userAdminAnyDatabase']})"
else
    echo "${CONTAINER_NAME} is already running."
fi
