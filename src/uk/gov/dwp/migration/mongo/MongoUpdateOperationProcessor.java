package uk.gov.dwp.migration.mongo;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;
import uk.gov.dwp.migration.mongo.api.DocumentMigrator;
import uk.gov.dwp.migration.mongo.kafka.api.MongoOperationProcessor;

public class MongoUpdateOperationProcessor implements MongoOperationProcessor<MongoUpdateMessage> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MongoUpdateOperationProcessor.class);

    private final MongoCollection<Document> mongoCollection;
    private final String db;
    private final String collection;
    private final MongoOperationKafkaMessageDispatcher mongoOperationKafaMessageDispatcher;
    private final DocumentMigrator documentMigrator;

    public MongoUpdateOperationProcessor(MongoCollection mongoCollection,
                                         String db,
                                         String collection,
                                         MongoOperationKafkaMessageDispatcher mongoOperationKafaMessageDispatcher,
                                         DocumentMigrator documentMigrator) {
        this.mongoCollection = mongoCollection;
        this.db = db;
        this.collection = collection;
        this.mongoOperationKafaMessageDispatcher = mongoOperationKafaMessageDispatcher;
        this.documentMigrator = documentMigrator;
    }

    @Override
    public void process(MongoUpdateMessage mongoUpdateMessage) {
        // Migrate the document (if appropriate) and write and "update" record to the Kafka feed
        // When migrating the document what do we set the _lastModifiedDateTime to be if we set it to now()
        Document document = documentMigrator.migrate(new Document(mongoUpdateMessage.getData()));
        try {
            UpdateResult updateResult = mongoCollection.updateOne(new Document("_id", document.get("_id")), document);
            if (updateResult.getModifiedCount() == 0) {
                mongoCollection.insertOne(document);
            }
            mongoOperationKafaMessageDispatcher.send(new MongoUpdateMessage(db, collection, document));
        } catch (MongoWriteException e) {
            // TODO: Should we try and findOneAndReplace??
            LOGGER.warn("Unable to update: {}", document.get("_id"));
        }
    }
}
