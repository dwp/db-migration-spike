package uk.gov.dwp.migration.mongo.kafka.consumer;

import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;
import uk.gov.dwp.migration.mongo.kafka.api.MongoOperationProcessor;

import java.util.ArrayList;
import java.util.List;

public class MongoOperationDelegatingProcessorRegistry implements MongoOperationProcessor<MongoOperation> {

    private final List<MongoOperationDelegatingProcessor> mongoOperationDelegatingProcessors = new ArrayList<>();

    public MongoOperationDelegatingProcessor add(MongoOperationDelegatingProcessor mongoOperationDelegatingProcessor) {
        mongoOperationDelegatingProcessors.add(mongoOperationDelegatingProcessor);
        return mongoOperationDelegatingProcessor;
    }

    @Override
    public void process(MongoOperation mongoOperation) {
        mongoOperationDelegatingProcessors
                .forEach(mongoOperationProcessor -> mongoOperationProcessor.process(mongoOperation));
    }
}
