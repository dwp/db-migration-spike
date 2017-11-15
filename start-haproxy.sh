#!/bin/bash

IMAGE_NAME="haproxy:1.7"
CONTAINER_NAME=haproxy-server

if [ ! "$(docker images ${IMAGE_NAME} | grep haproxy)" ]; then
    docker pull ${IMAGE_NAME}
fi

HOSTIP=`ip addr show | grep docker0 | grep global | awk '{print $2}' | cut -d / -f1`

if [ ! "$(docker ps -q -f name=${CONTAINER_NAME})" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=${CONTAINER_NAME})" ]; then
        echo "Cleaning up ${CONTAINER_NAME}"
        docker rm ${CONTAINER_NAME}
    fi
    echo "Running ${CONTAINER_NAME}"
    docker run --name ${CONTAINER_NAME} --detach --rm -p 8443:8443 --add-host=docker:${HOSTIP} -v `pwd`/haproxy.cfg:/usr/local/etc/haproxy/haproxy.cfg:ro ${IMAGE_NAME}
else
    echo "${CONTAINER_NAME} is already running."
fi
