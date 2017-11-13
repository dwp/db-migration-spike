package uk.gov.dwp.migration.kafka.consumer;

import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;
import uk.gov.dwp.migration.kafka.api.MongoOperationProcessor;

public class DbCollectionMongoOperationProcessor implements MongoOperationProcessor {

    private final String dbName;
    private final String collectionName;
    private final MongoOperationProcessor mongoOperationProcessor;

    public DbCollectionMongoOperationProcessor(String dbName,
                                               String collectionName,
                                               MongoOperationProcessor mongoOperationProcessor) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.mongoOperationProcessor = mongoOperationProcessor;
    }

    @Override
    public void process(MongoOperation mongoOperation) {
        if (dbName.equals(mongoOperation.getDb()) && collectionName.equals(mongoOperation.getCollection())) {
            mongoOperationProcessor.process(mongoOperation);
        }
    }
}
