package uk.gov.dwp.migration.kafka.api;

import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;

public interface MongoOperationProcessor<T extends MongoOperation> {

    void process(T mongoOperation);
}
