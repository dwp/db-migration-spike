#!/bin/bash

MONGO_DB_ADDRESS=${MONGO_DB_ADDRESS:-"localhost:27017/personal-details"}
MONGO_COLLECTION=${MONGO_COLLECTION:-"personalDetails"}
MONGO_ADMIN_USER=${MONGO_ADMIN_USER:-"admin"}
MONGO_ADMIN_PASSWORD=${MONGO_ADMIN_PASSWORD:-"Passw0rd"}
MONGO_ADMIN_DB=${MONGO_ADMIN_DB:-"admin"}

echo "Connecting to ${MONGO_DB_ADDRESS} as ${MONGO_ADMIN_USER}"

mongo --username=${MONGO_ADMIN_USER} \
      --password=${MONGO_ADMIN_PASSWORD} \
      --authenticationDatabase=${MONGO_ADMIN_DB} \
      ${MONGO_DB_ADDRESS} <<!
db.createUser({
  user: "personalDetailsUser",
  pwd: "Passw0rd",
  roles: [
    "personalDetailsReadWrite"
  ]
})
!