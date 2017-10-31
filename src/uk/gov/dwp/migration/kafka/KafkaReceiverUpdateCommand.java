package uk.gov.dwp.migration.kafka;

import com.mongodb.QueryOperators;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import uk.gov.dwp.migration.api.DocumentMigrator;
import uk.gov.dwp.migration.api.UpdateCommand;

public class KafkaReceiverUpdateCommand implements UpdateCommand<Document> {

    private final MongoCollection<org.bson.Document> mongoCollection;
    private final DocumentMigrator documentMigrator;

    public KafkaReceiverUpdateCommand(MongoCollection<Document> mongoCollection,
                                      DocumentMigrator documentMigrator) {
        this.mongoCollection = mongoCollection;
        this.documentMigrator = documentMigrator;
    }

    @Override
    public void update(Document document) {
        mongoCollection.replaceOne(createFilter(document), document);
    }

    private Document createFilter(Document document) {
        return new Document("_id", document.get("_id"))
                        .append("_lastModifiedDateTime", new Document(QueryOperators.GT, document.get("_lastModifiedDateTime")));
    }
}
