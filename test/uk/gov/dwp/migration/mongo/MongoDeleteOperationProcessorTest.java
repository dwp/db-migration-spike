package uk.gov.dwp.migration.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import uk.gov.dwp.common.kafka.mongo.api.MongoDeleteMessage;

import java.time.Clock;
import java.time.Instant;
import java.util.Collections;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.dwp.common.mongo.test.support.DocumentMatcher.hasField;

public class MongoDeleteOperationProcessorTest {

    private static final Instant NOW = Instant.now();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private MongoCollection<Document> destinationCollection;
    @Mock
    private MongoDeleteMessage mongoDeleteMessage;
    @Mock
    private UpdateResult updateResult;
    @Mock
    private Clock clock;

    private MongoDeleteOperationProcessor underTest;

    @Before
    public void setUp() {
        underTest = new MongoDeleteOperationProcessor(destinationCollection, clock);
        when(clock.instant()).thenReturn(NOW);
        when(mongoDeleteMessage.getData()).thenReturn(Collections.singletonMap("_id", "123"));
    }

    @Test
    public void deleteARecordThatHasAlreadyBeenMigrated() throws Exception {
        when(destinationCollection.replaceOne(any(Bson.class), any(Document.class))).thenReturn(updateResult);
        when(updateResult.getModifiedCount()).thenReturn(1L);

        underTest.process(mongoDeleteMessage);

        verify(destinationCollection).replaceOne(idEquals(), documentMatches(allOf(
                hasField("_id", equalTo("123")),
                hasField("_removedDateTime", equalTo(NOW)),
                hasField("_lastModifiedDateTime", equalTo(NOW))
        )));
        verify(destinationCollection, times(0)).insertOne(any(Document.class));

    }

    @Test
    public void deleteARecordThatHasNotYetBeenMigrated() throws Exception {
        when(destinationCollection.replaceOne(any(Bson.class), any(Document.class))).thenReturn(updateResult);
        when(updateResult.getModifiedCount()).thenReturn(0L);

        underTest.process(mongoDeleteMessage);

        verify(destinationCollection).replaceOne(idEquals(), documentMatches(allOf(
                hasField("_id", equalTo("123")),
                hasField("_removedDateTime", equalTo(NOW)),
                hasField("_lastModifiedDateTime", equalTo(NOW))
        )));
        verify(destinationCollection).insertOne(documentMatches(allOf(
                hasField("_id", equalTo("123")),
                hasField("_removedDateTime", equalTo(NOW)),
                hasField("_lastModifiedDateTime", equalTo(NOW))
        )));
    }

    private Document idEquals() {
        return Mockito.argThat(document -> document.get("_id").equals("123"));
    }

    private Document documentMatches(Matcher<Document> matcher) {
        return Mockito.argThat(new HamcrestArgumentMatcher<>(matcher));
    }
}