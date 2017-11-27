package uk.gov.dwp.migration.mongo;

import com.mongodb.MongoWriteException;
import com.mongodb.QueryOperators;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;
import uk.gov.dwp.migration.mongo.api.DocumentMigrator;
import uk.gov.dwp.migration.mongo.kafka.api.MongoOperationProcessor;

import java.util.Arrays;

import static com.mongodb.QueryOperators.EXISTS;
import static com.mongodb.QueryOperators.LT;

public class MongoUpdateOperationProcessor implements MongoOperationProcessor<MongoUpdateMessage> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MongoUpdateOperationProcessor.class);

    private final MongoCollection<Document> mongoCollection;
    private final String db;
    private final String collection;
    private final MongoOperationKafkaMessageDispatcher mongoOperationKafkaMessageDispatcher;
    private final DocumentMigrator documentMigrator;

    public MongoUpdateOperationProcessor(MongoCollection mongoCollection,
                                         String db,
                                         String collection,
                                         MongoOperationKafkaMessageDispatcher mongoOperationKafkaMessageDispatcher,
                                         DocumentMigrator documentMigrator) {
        this.mongoCollection = mongoCollection;
        this.db = db;
        this.collection = collection;
        this.mongoOperationKafkaMessageDispatcher = mongoOperationKafkaMessageDispatcher;
        this.documentMigrator = documentMigrator;
    }

    @Override
    public void process(MongoUpdateMessage mongoUpdateMessage) {
        // When migrating the document what do we set the _lastModifiedDateTime to be if we set it to now()
        Document document = documentMigrator.migrate(new Document(mongoUpdateMessage.getData()));
        try {
            UpdateResult updateResult = mongoCollection.replaceOne(buildReplaceFilter(document, document.get("_id")), document);
            if (updateResult.getModifiedCount() == 0) {
                mongoCollection.insertOne(document);
            }
            mongoOperationKafkaMessageDispatcher.send(new MongoUpdateMessage(db, collection, document));
        } catch (MongoWriteException e) {
            // TODO: Should we try and findOneAndReplace??
            LOGGER.warn("Unable to update: {}", document.get("_id"));
        }
    }

    private Document buildReplaceFilter(Document document, Object id) {
        Document filter = new Document("_id", id);
        if (document.containsKey("_lastModifiedDateTime")) {
            filter.append(QueryOperators.OR, Arrays.asList(
                    new Document("_lastModifiedDateTime", new Document(LT, document.get("_lastModifiedDateTime"))),
                    new Document("_lastModifiedDateTime", new Document(EXISTS, false))
            ));
        }
        return filter;
    }
}
