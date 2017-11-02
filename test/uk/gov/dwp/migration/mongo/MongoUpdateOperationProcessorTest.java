package uk.gov.dwp.migration.mongo;

import com.mongodb.client.MongoCollection;
import org.junit.Test;
import uk.gov.dwp.migration.api.DocumentMigrator;
import uk.gov.dwp.migration.kafka.producer.MongoOperationKafkaMessageDispatcher;

import static org.mockito.Mockito.mock;

public class MongoUpdateOperationProcessorTest {

    private static final String DB_NAME = "db";
    private static final String COLLECTION_NAME = "collection";

    private final MongoOperationKafkaMessageDispatcher mongoOperationKafaMessageDispatcher = mock(MongoOperationKafkaMessageDispatcher.class);
    private final MongoCollection mongoCollection = mock(MongoCollection.class);
    private final DocumentMigrator documentMigrator = mock(DocumentMigrator.class);

    private final MongoUpdateOperationProcessor underTest = new MongoUpdateOperationProcessor(mongoCollection, DB_NAME, COLLECTION_NAME, mongoOperationKafaMessageDispatcher, documentMigrator);

    @Test
    public void name() throws Exception {

    }
}