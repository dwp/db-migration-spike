package uk.gov.dwp.migration.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.common.kafka.mongo.api.MongoDeleteMessage;
import uk.gov.dwp.migration.mongo.kafka.api.MongoOperationProcessor;

import java.time.Instant;

public class MongoDeleteOperationProcessor implements MongoOperationProcessor<MongoDeleteMessage> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MongoInsertOperationProcessor.class);

    private final MongoCollection<Document> mongoCollection;

    public MongoDeleteOperationProcessor(MongoCollection<Document> mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    @Override
    public void process(MongoDeleteMessage mongoDeleteMessage) {
        UpdateResult updateResult = mongoCollection.replaceOne(
                createId(mongoDeleteMessage), new Document("_deleted", true).append("_lastModifiedDateTime", Instant.now())
        );
        if (updateResult.getModifiedCount() == 0) {
            mongoCollection.insertOne(createId(mongoDeleteMessage)
                    .append("_deleted", true)
                    .append("_lastModifiedDateTime", Instant.now())
            );
        }
    }

    private Document createId(MongoDeleteMessage mongoOperation) {
        return new Document("_id", mongoOperation.getData().get("_id"));
    }
}
