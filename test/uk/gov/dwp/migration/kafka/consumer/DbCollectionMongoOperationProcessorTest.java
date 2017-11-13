package uk.gov.dwp.migration.kafka.consumer;

import org.junit.Test;
import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;
import uk.gov.dwp.migration.kafka.api.MongoOperationProcessor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class DbCollectionMongoOperationProcessorTest {

    private static final String DB_NAME = "db";
    private static final String COLLECTION_NAME = "collection";

    private final MongoOperationProcessor mongoOperationProcessor = mock(MongoOperationProcessor.class);
    private final MongoOperation mongoOperation = mock(MongoOperation.class);

    private final DbCollectionMongoOperationProcessor underTest = new DbCollectionMongoOperationProcessor(
            DB_NAME,
            COLLECTION_NAME,
            mongoOperationProcessor
    );

    @Test
    public void mongoOperationIsOnlyPassedToDelegateIfDbAndCollectionNamesMatch() {
        when(mongoOperation.getDb()).thenReturn(DB_NAME);
        when(mongoOperation.getCollection()).thenReturn(COLLECTION_NAME);

        underTest.process(mongoOperation);

        verify(mongoOperationProcessor).process(mongoOperation);
    }

    @Test
    public void mongoOperationIsNotPassedToDelegateIfDbNamesDoNotMatch() {
        when(mongoOperation.getDb()).thenReturn("another-db");

        underTest.process(mongoOperation);

        verifyZeroInteractions(mongoOperationProcessor);
    }

    @Test
    public void mongoOperationIsNotPassedToDelegateIfCollectionNamesDoNotMatch() {
        when(mongoOperation.getDb()).thenReturn(DB_NAME);
        when(mongoOperation.getCollection()).thenReturn("another-collection");

        underTest.process(mongoOperation);

        verifyZeroInteractions(mongoOperationProcessor);
    }
}