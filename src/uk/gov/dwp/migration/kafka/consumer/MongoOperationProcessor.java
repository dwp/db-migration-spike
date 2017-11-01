package uk.gov.dwp.migration.kafka.consumer;

import uk.gov.dwp.migration.kafka.api.MongoOperation;

public interface MongoOperationProcessor {

    void process(MongoOperation mongoOperation);
}
