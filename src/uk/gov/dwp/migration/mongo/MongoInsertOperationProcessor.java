package uk.gov.dwp.migration.mongo;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.common.kafka.mongo.api.MongoInsertMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;
import uk.gov.dwp.migration.api.DocumentMigrator;
import uk.gov.dwp.migration.kafka.api.MongoOperationProcessor;

public class MongoInsertOperationProcessor implements MongoOperationProcessor<MongoInsertMessage> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MongoInsertOperationProcessor.class);

    private final MongoCollection<Document> mongoCollection;
    private final String db;
    private final String collection;
    private final MongoOperationKafkaMessageDispatcher mongoOperationKafaMessageDispatcher;
    private final DocumentMigrator documentMigrator;

    public MongoInsertOperationProcessor(MongoCollection mongoCollection,
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
    public void process(MongoInsertMessage mongoInsertMessage) {
        // Migrate the document (if appropriate) and write and "update" record to the Kafka feed
        // When migrating the document what do we set the _lastModifiedDateTime to be if we set it to now()
        Document document = documentMigrator.migrate(new Document(mongoInsertMessage.getData()));
        try {
            mongoCollection.insertOne(document);
            mongoOperationKafaMessageDispatcher.send(new MongoUpdateMessage(db, collection, document));
        } catch (MongoWriteException e) {
            LOGGER.warn("Document: {} already exists", document.get("_id"));
        }
    }
}
