package uk.gov.dwp.migration.mongo;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.migration.api.DocumentMigrator;
import uk.gov.dwp.migration.kafka.api.MongoInsertMessage;
import uk.gov.dwp.migration.kafka.api.MongoOperation;
import uk.gov.dwp.migration.kafka.api.MongoOperationProcessor;
import uk.gov.dwp.migration.kafka.api.MongoUpdateMessage;
import uk.gov.dwp.migration.kafka.producer.MongoOperationKafaMessageDispatcher;

public class MongoInsertOperationProcessor implements MongoOperationProcessor {

    private final static Logger LOGGER = LoggerFactory.getLogger(MongoInsertOperationProcessor.class);

    private final MongoCollection<Document> mongoCollection;
    private final String db;
    private final String collection;
    private final MongoOperationKafaMessageDispatcher mongoOperationKafaMessageDispatcher;
    private final DocumentMigrator documentMigrator;

    public MongoInsertOperationProcessor(MongoCollection mongoCollection,
                                         String db,
                                         String collection,
                                         MongoOperationKafaMessageDispatcher mongoOperationKafaMessageDispatcher,
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
        Document document = documentMigrator.migrate(new Document(((MongoInsertMessage) mongoOperation).getDbObject()));
        try {
            mongoCollection.insertOne(document);
            mongoOperationKafaMessageDispatcher.send(new MongoUpdateMessage(db, collection, document));
        } catch (MongoWriteException e) {
            LOGGER.warn("Document: {} already exists", document.get("_id"));
        }
    }
}
