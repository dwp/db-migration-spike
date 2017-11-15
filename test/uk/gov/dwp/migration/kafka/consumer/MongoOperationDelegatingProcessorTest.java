package uk.gov.dwp.migration.kafka.consumer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import uk.gov.dwp.common.kafka.mongo.api.MongoDeleteMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoInsertMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;
import uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage;
import uk.gov.dwp.migration.kafka.api.MongoOperationProcessor;

import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MongoOperationDelegatingProcessorTest {

    private static final String DB_NAME = "db-name";
    private static final String COLLECTION_NAME = "collection-name";
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private MongoOperationProcessor<MongoInsertMessage> insertOperationProcessor;
    @Mock
    private MongoOperationProcessor<MongoUpdateMessage> updateOperationProcessor;
    @Mock
    private MongoOperationProcessor<MongoDeleteMessage> deleteOperationProcessor;
    @Mock
    private MongoOperationProcessor<MongoOperation> defaultMongoOperationProcessor;

    private MongoOperationDelegatingProcessor underTest;

    @Before
    public void setUp() {
        underTest = new MongoOperationDelegatingProcessor(
                DB_NAME,
                COLLECTION_NAME,
                insertOperationProcessor,
                updateOperationProcessor,
                deleteOperationProcessor,
                defaultMongoOperationProcessor
        );
    }

    @Test
    public void zeroInteractionsWithOperationProcessorsWhenDatabaseNamesDoNotMatch() throws Exception {
        MongoOperation mongoOperation = mock(MongoOperation.class);
        when(mongoOperation.getDb()).thenReturn("another-db");

        underTest.process(mongoOperation);

        verifyZeroInteractions(insertOperationProcessor, updateOperationProcessor, deleteOperationProcessor,defaultMongoOperationProcessor);
    }

    @Test
    public void zeroInteractionsWithOperationProcessorsWhenCollectionNamesDoNotMatch() throws Exception {
        MongoOperation mongoOperation = mock(MongoOperation.class);
        when(mongoOperation.getDb()).thenReturn(DB_NAME);
        when(mongoOperation.getCollection()).thenReturn("another-collection");

        underTest.process(mongoOperation);

        verifyZeroInteractions(insertOperationProcessor, updateOperationProcessor, deleteOperationProcessor, defaultMongoOperationProcessor);
    }

    @Test
    public void successfullyProcessMongoInsertMessage() throws Exception {
        MongoInsertMessage mongoInsertMessage = new MongoInsertMessage(DB_NAME, COLLECTION_NAME, emptyMap());

        underTest.process(mongoInsertMessage);

        verify(insertOperationProcessor).process(mongoInsertMessage);
        verifyZeroInteractions(updateOperationProcessor, deleteOperationProcessor, deleteOperationProcessor);
    }

    @Test
    public void successfullyProcessMongoUpdateMessage() throws Exception {
        MongoUpdateMessage mongoUpdateMessage = new MongoUpdateMessage(DB_NAME, COLLECTION_NAME, emptyMap());

        underTest.process(mongoUpdateMessage);

        verify(updateOperationProcessor).process(mongoUpdateMessage);
        verifyZeroInteractions(insertOperationProcessor, deleteOperationProcessor, deleteOperationProcessor);
    }

    @Test
    public void successfullyProcessMongoDeleteMessage() throws Exception {
        MongoDeleteMessage mongoDeleteMessage = new MongoDeleteMessage(DB_NAME, COLLECTION_NAME, emptyMap());

        underTest.process(mongoDeleteMessage);

        verify(deleteOperationProcessor).process(mongoDeleteMessage);
        verifyZeroInteractions(insertOperationProcessor, updateOperationProcessor, deleteOperationProcessor);
    }

    @Test
    public void processMongoOperationWithNoProcessor() throws Exception {
        MongoOperation mongoOperation = mock(MongoOperation.class);
        when(mongoOperation.getDb()).thenReturn(DB_NAME);
        when(mongoOperation.getCollection()).thenReturn(COLLECTION_NAME);

        underTest.process(mongoOperation);

        verify(defaultMongoOperationProcessor).process(mongoOperation);
        verifyZeroInteractions(insertOperationProcessor, updateOperationProcessor, deleteOperationProcessor);
    }


}