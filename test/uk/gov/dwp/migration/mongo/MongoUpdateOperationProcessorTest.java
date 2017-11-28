package uk.gov.dwp.migration.mongo;

import com.google.common.collect.ImmutableMap;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage;
import uk.gov.dwp.common.kafka.mongo.api.test.support.MongoOperationMatcher;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;
import uk.gov.dwp.migration.mongo.api.DocumentMigrator;

import java.time.Instant;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MongoUpdateOperationProcessorTest {

    private static final String DB_NAME = "db";
    private static final String COLLECTION_NAME = "collection";
    protected static final Instant NOW = Instant.now();
    protected static final String ID = "123";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private MongoCollection<Document> destinationCollection;
    @Mock
    private MongoUpdateMessage mongoUpdateMessage;
    @Mock
    private MongoOperationKafkaMessageDispatcher mongoOperationKafkaMessageDispatcher;
    @Mock
    private DocumentMigrator documentMigrator;
    @Mock
    private Document replaceFilter;
    @Mock
    private Document migratedDocument;
    @Mock
    private UpdateResult updateResult;
    @Mock
    private ReplaceFilterQueryFactory replaceFilterQueryFactory;

    private MongoUpdateOperationProcessor underTest;

    @Before
    public void setUp() {
        underTest = new MongoUpdateOperationProcessor(
                destinationCollection,
                DB_NAME,
                COLLECTION_NAME,
                mongoOperationKafkaMessageDispatcher,
                documentMigrator,
                replaceFilterQueryFactory);

        ImmutableMap<String, Object> data = ImmutableMap.of("_id", ID, "_lastModifiedDateTime", NOW);
        when(mongoUpdateMessage.getData()).thenReturn(data);
        when(documentMigrator.migrate(new Document(data))).thenReturn(migratedDocument);
        when(replaceFilterQueryFactory.build(migratedDocument, NOW)).thenReturn(replaceFilter);
        when(destinationCollection.replaceOne(replaceFilter, migratedDocument)).thenReturn(updateResult);
    }

    @Test
    public void processUpdateOperationForRecordNotYetMigrated() throws Exception {
        when(updateResult.getModifiedCount()).thenReturn(0L);

        underTest.process(mongoUpdateMessage);

        verify(destinationCollection).replaceOne(replaceFilter,  migratedDocument);
        verify(destinationCollection).insertOne(migratedDocument);
        verify(mongoOperationKafkaMessageDispatcher).send(Mockito.argThat(new HamcrestArgumentMatcher<>(MongoOperationMatcher.mongoUpdateOperation(equalTo(migratedDocument))
                .withDbName(DB_NAME)
                .withCollectionName(COLLECTION_NAME))));
    }

    @Test
    public void processUpdateOperationForMigratedRecord() throws Exception {
        when(updateResult.getModifiedCount()).thenReturn(1L);

        underTest.process(mongoUpdateMessage);

        verify(destinationCollection).replaceOne(replaceFilter,  migratedDocument);
        verifyNoMoreInteractions(destinationCollection);
        verify(mongoOperationKafkaMessageDispatcher).send(Mockito.argThat(new HamcrestArgumentMatcher<>(MongoOperationMatcher.mongoUpdateOperation(equalTo(migratedDocument))
                .withDbName(DB_NAME)
                .withCollectionName(COLLECTION_NAME))));
    }
}