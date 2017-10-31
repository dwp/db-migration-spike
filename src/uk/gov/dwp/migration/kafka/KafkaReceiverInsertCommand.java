package uk.gov.dwp.migration.kafka;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import uk.gov.dwp.migration.api.DocumentMigrator;
import uk.gov.dwp.migration.api.InsertCommand;

public class KafkaReceiverInsertCommand implements InsertCommand<Document> {

    private final MongoCollection<Document> mongoCollection;
    private final DocumentMigrator documentMigrator;

    public KafkaReceiverInsertCommand(MongoCollection<Document> mongoCollection,
                                      DocumentMigrator documentMigrator) {
        this.mongoCollection = mongoCollection;
        this.documentMigrator = documentMigrator;
    }

    @Override
    public void insert(Document original) {
        try {
            // Migrate the document (if appropriate) and write and "update" record to the Kafka feed
            Document document = documentMigrator.migrate(original);
            // When migrating the document what do we set the _lastModifiedDateTime to be if we set it to now()
            // how do we tell
            mongoCollection.insertOne(document);
        } catch (MongoWriteException e) {

        }
    }
}
