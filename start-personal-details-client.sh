#!/bin/bash

#!/bin/bash
IMAGE_NAME="personal-details-client:1.0"
CONTAINER_NAME="personal-details-client-v1"

# TODO: Consider adding docker build as a pre-step

if [ ! "$(docker ps -q -f name=${CONTAINER_NAME})" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=${CONTAINER_NAME})" ]; then
        echo "Cleaning up ${CONTAINER_NAME}"
        docker rm ${CONTAINER_NAME}
    fi
    echo "Running ${CONTAINER_NAME}"
    docker run --name ${CONTAINER_NAME} --link personal-details-server-v1 -e PERSONAL_DETAILS_BASE_URL='http://personal-details-server-v1:8008/personal-details' -it ${IMAGE_NAME}
else
    echo "${CONTAINER_NAME} is already running."
fi
