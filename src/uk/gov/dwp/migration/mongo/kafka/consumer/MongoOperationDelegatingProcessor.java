package uk.gov.dwp.migration.mongo.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.common.kafka.mongo.api.MongoDeleteMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoInsertMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;
import uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage;
import uk.gov.dwp.migration.mongo.kafka.api.MongoOperationProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MongoOperationDelegatingProcessor implements MongoOperationProcessor<MongoOperation> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoOperationDelegatingProcessor.class);

    private final String dbName;
    private final String collectionName;
    private final Map<Class<? extends MongoOperation>, MongoOperationProcessor> processors = new HashMap<>();
    private final MongoOperationProcessor<? extends MongoOperation> defaultMongoOperationProcessor;

    public MongoOperationDelegatingProcessor(String dbName,
                                             String collectionName,
                                             MongoOperationProcessor<MongoInsertMessage> insertMessageProcessor,
                                             MongoOperationProcessor<MongoUpdateMessage> updateMessageProcessor,
                                             MongoOperationProcessor<MongoDeleteMessage> deleteMessageProcessor) {
        this(dbName, collectionName, insertMessageProcessor, updateMessageProcessor, deleteMessageProcessor,
                mongoOperation -> LOGGER.warn("MongoOperationProcessor not defined for {} on {}.{}",
                        mongoOperation.getClass(),
                        mongoOperation.getDb(),
                        mongoOperation.getCollection()));
    }

    MongoOperationDelegatingProcessor(String dbName,
                                      String collectionName,
                                      MongoOperationProcessor<MongoInsertMessage> insertMessageProcessor,
                                      MongoOperationProcessor<MongoUpdateMessage> updateMessageProcessor,
                                      MongoOperationProcessor<MongoDeleteMessage> deleteMessageProcessor,
                                      MongoOperationProcessor<? extends MongoOperation> defaultMongoOperationProcessor) {
        this.dbName = Objects.requireNonNull(dbName);
        this.collectionName = Objects.requireNonNull(collectionName);
        this.processors.put(MongoInsertMessage.class, Objects.requireNonNull(insertMessageProcessor));
        this.processors.put(MongoUpdateMessage.class, Objects.requireNonNull(updateMessageProcessor));
        this.processors.put(MongoDeleteMessage.class, Objects.requireNonNull(deleteMessageProcessor));
        this.defaultMongoOperationProcessor = defaultMongoOperationProcessor;

    }

    @Override
    public void process(MongoOperation mongoOperation) {
        if (dbName.equals(mongoOperation.getDb()) && collectionName.equals(mongoOperation.getCollection())) {
            processors.getOrDefault(mongoOperation.getClass(), defaultMongoOperationProcessor).process(mongoOperation);
        }
    }
}
