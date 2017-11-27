package uk.gov.dwp.migration.mongo;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;
import uk.gov.dwp.migration.mongo.api.DocumentWriter;
import uk.gov.dwp.migration.mongo.api.Migration;

public class MongoDocumentWriter implements DocumentWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDocumentWriter.class);

    private final MongoCollection<Document> mongoCollection;
    private final MongoOperationKafkaMessageDispatcher mongoOperationKafkaMessageDispatcher;

    public MongoDocumentWriter(MongoCollection<Document> mongoCollection,
                               MongoOperationKafkaMessageDispatcher mongoOperationKafkaMessageDispatcher) {
        this.mongoCollection = mongoCollection;
        this.mongoOperationKafkaMessageDispatcher = mongoOperationKafkaMessageDispatcher;
    }

    @Override
    public void writeDocument(Migration migration, Document document) {
        migration.recordMigrated();
        try {
            // TODO: Should we "force" an update of _lastModifiedDateTime
            mongoCollection.insertOne(document);
            mongoOperationKafkaMessageDispatcher.send(new MongoUpdateMessage(
                    mongoCollection.getNamespace().getDatabaseName(),
                    mongoCollection.getNamespace().getCollectionName(),
                    document
            ));
        } catch (MongoWriteException e) {
            // TODO: Is this the right level?  Probably not
            LOGGER.warn("Document with _id: {} already exists", document.get("_id"));
        }
    }
}
