package uk.gov.dwp.migration.mongo;

import org.bson.Document;
import org.junit.Test;

import java.time.Instant;

import static com.mongodb.QueryOperators.EXISTS;
import static com.mongodb.QueryOperators.LT;
import static com.mongodb.QueryOperators.OR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static uk.gov.dwp.common.mongo.test.support.DocumentMatcher.hasField;

public class ReplaceFilterQueryFactoryTest {

    private static final Instant LAST_MODIFIED_DATE_TIME = Instant.now();

    private final ReplaceFilterQueryFactory underTest = new ReplaceFilterQueryFactory();

    @Test
    public void buildFilterWithNoLastModifiedDateTime() throws Exception {
        Document filter = underTest.build(
                new Document("_id", "123"),
                null
        );
        assertThat(filter, hasField("_id", equalTo("123")));
    }

    @Test
    public void buildFilterWithLastModifiedDateTime() throws Exception {
        Document filter = underTest.build(
                new Document("_id", "123"),
                LAST_MODIFIED_DATE_TIME
        );
        assertThat(filter, allOf(
                hasField("_id", equalTo("123")),
                hasField(OR, contains(
                        hasField("_lastModifiedDateTime", hasField(LT, equalTo(LAST_MODIFIED_DATE_TIME))),
                        hasField("_lastModifiedDateTime", hasField(EXISTS, equalTo(false)))
                ))
        ));
    }
}