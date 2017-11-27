package uk.gov.dwp.migration.mongo;

import com.mongodb.MongoNamespace;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;
import uk.gov.dwp.common.kafka.mongo.api.test.support.MongoOperationMatcher;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;
import uk.gov.dwp.migration.mongo.api.Migration;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.dwp.common.kafka.mongo.api.test.support.MongoOperationMatcher.mongoUpdateOperation;

public class MongoDocumentWriterTest {

    private static final String DB_NAME = "personal-details";
    private static final String COLLECTION_NAME = "personalDetails";
    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private MongoCollection<Document> mongoCollection;
    @Mock
    private Migration migration;
    @Mock
    private Document document;
    @Mock
    private MongoOperationKafkaMessageDispatcher mongoOperationKafkaMessageDispatcher;
    private MongoNamespace mongoNamespace = new MongoNamespace(DB_NAME, COLLECTION_NAME);

    private MongoDocumentWriter underTest;

    @Before
    public void setUp() {
        underTest = new MongoDocumentWriter(mongoCollection, mongoOperationKafkaMessageDispatcher);
    }

    @Test
    public void successfullyWriteDocument() throws Exception {
        when(mongoCollection.getNamespace()).thenReturn(mongoNamespace);

        underTest.writeDocument(migration, document);

        verify(migration).recordMigrated();
        verify(mongoCollection).insertOne(document);
        verify(mongoOperationKafkaMessageDispatcher).send(matches(mongoUpdateOperation(Matchers.sameInstance(document))));
    }

    @Test
    public void duplicateRecordIsIgnored() throws Exception {
        doThrow(MongoWriteException.class).when(mongoCollection).insertOne(document);

        underTest.writeDocument(migration, document);

        verify(migration).recordMigrated();
        verify(mongoCollection).insertOne(document);
        verifyZeroInteractions(mongoOperationKafkaMessageDispatcher);
    }

    private MongoOperation matches(MongoOperationMatcher<? extends MongoOperation> mongoOperationMatcher) {
        return Mockito.argThat(new HamcrestArgumentMatcher<>(mongoOperationMatcher
                .withDbName(DB_NAME)
                .withCollectionName(COLLECTION_NAME)
        ));
    }
}
