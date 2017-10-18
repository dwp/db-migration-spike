package uk.gov.dwp.migration.mongo;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.migration.api.DocumentWriter;
import uk.gov.dwp.migration.api.Migration;

public class MongoDocumentWriter implements DocumentWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDocumentWriter.class);

    private final MongoCollection<Document> mongoCollection;

    public MongoDocumentWriter(MongoCollection<Document> mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    @Override
    public void writeDocument(Migration migration, Document document) {
        migration.recordMigrated();
        try {
            // TODO: Should we set/update _lastModifiedDateTime
            mongoCollection.insertOne(document);
        } catch (MongoWriteException e) {
            // TODO: Is this the right level?  Probably not
            LOGGER.warn("Document with _id: {} already exists", document.get("_id"));
        }
    }
}
