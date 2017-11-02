package uk.gov.dwp.migration.kafka.api;

public interface MongoOperationProcessor {

    void process(MongoOperation mongoOperation);
}
