package uk.gov.dwp.migration.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.common.kafka.mongo.api.MongoDeleteMessage;
import uk.gov.dwp.migration.mongo.kafka.api.MongoOperationProcessor;

import java.time.Clock;

public class MongoDeleteOperationProcessor implements MongoOperationProcessor<MongoDeleteMessage> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MongoInsertOperationProcessor.class);

    private final MongoCollection<Document> destinationCollection;
    private final Clock clock;

    public MongoDeleteOperationProcessor(MongoCollection<Document> destinationCollection) {
        this(destinationCollection, Clock.systemUTC());
    }

    MongoDeleteOperationProcessor(MongoCollection<Document> destinationCollection,
                                  Clock clock) {
        this.destinationCollection = destinationCollection;
        this.clock = clock;
    }

    @Override
    public void process(MongoDeleteMessage mongoDeleteMessage) {
        Document removedDocument = createRemovedDocument(mongoDeleteMessage);
        UpdateResult updateResult = destinationCollection.replaceOne(
                createId(mongoDeleteMessage),
                removedDocument
        );
        if (updateResult.getModifiedCount() == 0) {
            destinationCollection.insertOne(removedDocument);
        }
    }

    private Document createRemovedDocument(MongoDeleteMessage mongoDeleteMessage) {
        return createId(mongoDeleteMessage)
                .append("_removedDateTime", clock.instant())
                .append("_lastModifiedDateTime", clock.instant());
    }

    private Document createId(MongoDeleteMessage mongoOperation) {
        return new Document("_id", mongoOperation.getData().get("_id"));
    }
}
