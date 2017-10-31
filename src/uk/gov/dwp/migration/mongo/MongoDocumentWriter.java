package uk.gov.dwp.migration.mongo;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.migration.api.DocumentWriter;
import uk.gov.dwp.migration.api.Migration;

public class MongoDocumentWriter implements DocumentWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDocumentWriter.class);

    private final MongoCollection<Document> mongoCollection;
    // TODO: What type is currently being sent
    private final Producer<String, Document> producer;

    public MongoDocumentWriter(MongoCollection<Document> mongoCollection,
                               Producer<String, Document> producer) {
        this.mongoCollection = mongoCollection;
        this.producer = producer;
    }

    @Override
    public void writeDocument(Migration migration, Document document) {
        migration.recordMigrated();
        try {
            // TODO: Should we set/update _lastModifiedDateTime
            mongoCollection.insertOne(document);
            producer.send(new ProducerRecord<>("topic", document));
        } catch (MongoWriteException e) {
            LOGGER.warn("Document with _id: {} already exists", document.get("_id"));
        }
    }
}
