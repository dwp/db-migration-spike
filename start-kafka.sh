#!/bin/bash
HOSTIP=`ip addr show | grep docker0 | grep global | awk '{print $2}' | cut -d / -f1`

docker run -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=$HOSTIP --env ADVERTISED_PORT=9092 spotify/kafka
