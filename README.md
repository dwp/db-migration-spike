# db-migration-spike

This spike consists of:
- a simple microservice `personal-details-server` which exposes the following CRUD operations via REST over HTTP (see 
`uk.gov.dwp.personal.details.api.PersonalDetailsClient` for further details):
  - `GET /personal-details/{personalDetailsId}`
  - `POST /personal-details`
  - `PUT /personal-details`
  - 'DELETE /personal-details/{personalDetailsId'
- a simple "client": `personal-details-client` which hits each of the HTTP endpoints of `personal-details-server` at regular intervals
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

## Running
### Starting Mongo
```bash
./start-mongo.sh
```

### Starting Kafka
```bash
./start-mongo.sh
```

### Starting HA Proxy
```bash
./start-haproxy.sh
```
