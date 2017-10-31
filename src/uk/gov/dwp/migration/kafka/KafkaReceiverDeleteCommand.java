package uk.gov.dwp.migration.kafka;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import uk.gov.dwp.migration.api.DeleteCommand;

public class KafkaReceiverDeleteCommand implements DeleteCommand<Document> {

    private final MongoCollection<Document> mongoCollection;

    public KafkaReceiverDeleteCommand(MongoCollection<Document> mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    @Override
    public void delete(Document document) {

    }
}
