package uk.gov.dwp.migration.kafka;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.Test;

import java.util.UUID;

import static org.mockito.Mockito.mock;

public class KafkaReceiverInsertCommandTest {

    private static final String ID = UUID.randomUUID().toString();

    private final MongoCollection mongoCollection = mock(MongoCollection.class);

    private final KafkaReceiverInsertCommand underTest = new KafkaReceiverInsertCommand(mongoCollection, documentMigrator);

    @Test
    public void insertWhenRecordDoesNotExist() throws Exception {
        underTest.insert(new Document("_id", ID));
    }


}