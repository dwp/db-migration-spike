# db-migration-spike

This spike consists of:
- a simple microservice `personal-details-server` which exposes the following CRUD operations via REST over HTTP (see 
`uk.gov.dwp.personal.details.api.PersonalDetailsClient` for further details):
  - `GET /personal-details/{personalDetailsId}`
  - `POST /personal-details`
  - `PUT /personal-details`
  - `DELETE /personal-details/{personalDetailsId`
- a simple "client": `personal-details-client` which hits each of the HTTP endpoints of `personal-details-server` at regular intervals
 (see `uk.gov.dwp.personal.details.client.PersonalDetailsClientApplication` for further details).
- a standalone migration microservice which:
  - Does something
  
  
Two core types of Migration:
- Bait-and-Switch
- On-the-fly

## On-the-fly Migration
Data is written back to the original collection, the data written must be backwards compatible

## Bait-and-Switch Migration
Data is written to a separate collection 

Migration
Consists of 3 core parts:
- DocumentSelector - identifies
- DocumentMigrator
- DocumentWriter
KafkaConsumer listens to topic and consumes any CRUD operations

Once the migration is complete

## Building
This project uses [Buck](https://buckbuild.com/) as a build tool.  Please refer to the [Getting Started Guide]() for installation and usage of Buck.
This build has been tested using `v2017.10.01.01`

```bash
buck build src/uk/gov/dwp/personal/details/server:personal-details-docker

#TODO: Move the following step to be part of the buck build process
docker build --tag=personal-details-server --file=buck-out/gen/src/uk/gov/dwp/personal/details/server/personal-details-docker/Dockerfile .
```

### Starting Mongo
Start a Docker container running an authenticated Mongo instance.

```bash
./start-mongo.sh
```
Connecting to the mongo instance from the host OS
```bash
mongo -username=admin -password=Passw0rd -authenticationDatabase=admin localhost:28018/admin
```

### Starting Kafka
```bash
./start-kafka.sh
```

### Starting HA Proxy
```bash
./start-haproxy.sh
```

### Start the Personal Details Server
Start the Personal Details Microservice running in a Docker container
```bash
./start-personal-details.sh
```
If you want to connect to the Docker container execute the following command:
```bash
docker exec -it personal-details-server-v1 /bin/bash
```

### Start the Personal Details Client
Start an example client which will start making HTTP requests to the `personal-details-server`
```bash
./start-personal-details-client.sh
```