package uk.gov.dwp.migration.mongo;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import uk.gov.dwp.common.kafka.mongo.api.MongoInsertMessage;
import uk.gov.dwp.common.kafka.mongo.api.test.support.MongoOperationMatcher;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;
import uk.gov.dwp.migration.mongo.api.DocumentMigrator;

import java.util.Collections;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MongoInsertOperationProcessorTest {

    private static final String DB_NAME = "db-name";
    private static final String COLLECTION_NAME = "collectionName";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private MongoCollection<Document> destinationCollection;
    @Mock
    private MongoInsertMessage mongoInsertMessage;
    @Mock
    private MongoOperationKafkaMessageDispatcher mongoOperationKafkaMessageDispatcher;
    @Mock
    private DocumentMigrator documentMigrator;
    @Mock
    private Document migratedDocument;

    private MongoInsertOperationProcessor underTest;

    @Before
    public void setUp() {
        underTest = new MongoInsertOperationProcessor(
                destinationCollection,
                DB_NAME,
                COLLECTION_NAME,
                mongoOperationKafkaMessageDispatcher,
                documentMigrator);
        when(mongoInsertMessage.getData()).thenReturn(Collections.singletonMap("_id", "123"));
    }

    @Test
    public void processARecordThatHasNotYetBeenMigrated() throws Exception {
        when(documentMigrator.migrate(new Document(Collections.singletonMap("_id", "123")))).thenReturn(migratedDocument);

        underTest.process(mongoInsertMessage);

        verify(destinationCollection).insertOne(migratedDocument);
        verify(mongoOperationKafkaMessageDispatcher).send(Mockito.argThat(new HamcrestArgumentMatcher<>(MongoOperationMatcher.mongoUpdateOperation(notNullValue(Document.class))
                .withDbName(DB_NAME)
                .withCollectionName(COLLECTION_NAME))));
    }

    @Test
    public void processARecordThatHasAlreadyBeenMigrated() throws Exception {
        when(documentMigrator.migrate(new Document(Collections.singletonMap("_id", "123")))).thenReturn(migratedDocument);
        doThrow(MongoWriteException.class).when(destinationCollection).insertOne(migratedDocument);

        underTest.process(mongoInsertMessage);

        verify(destinationCollection).insertOne(migratedDocument);
        verifyZeroInteractions(mongoOperationKafkaMessageDispatcher);
    }
}