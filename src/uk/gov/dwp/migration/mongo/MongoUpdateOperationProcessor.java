package uk.gov.dwp.migration.mongo;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.migration.api.DocumentMigrator;
import uk.gov.dwp.migration.kafka.api.MongoOperation;
import uk.gov.dwp.migration.kafka.api.MongoOperationProcessor;
import uk.gov.dwp.migration.kafka.api.MongoUpdateMessage;
import uk.gov.dwp.migration.kafka.producer.MongoOperationKafkaMessageDispatcher;

public class MongoUpdateOperationProcessor implements MongoOperationProcessor {

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
    public void process(MongoOperation mongoOperation) {
        // Migrate the document (if appropriate) and write and "update" record to the Kafka feed
        // When migrating the document what do we set the _lastModifiedDateTime to be if we set it to now()
        Document document = documentMigrator.migrate(new Document(((MongoUpdateMessage) mongoOperation).getDbObject()));
        try {
            Document originalDocument = mongoCollection.findOneAndReplace(new Document("_id", document.get("_id")), document);
            if (originalDocument == null) {
                mongoCollection.insertOne(document);
            }
            mongoOperationKafaMessageDispatcher.send(new MongoUpdateMessage(db, collection, document));
        } catch (MongoWriteException e) {
            // TODO: Should we try and findOneAndReplace??
            LOGGER.warn("Unable to update: {}", document.get("_id"));
        }
    }
}
