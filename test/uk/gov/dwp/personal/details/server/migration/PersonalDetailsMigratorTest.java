package uk.gov.dwp.personal.details.server.migration;

import org.bson.Document;
import org.junit.Test;
import uk.gov.dwp.personal.details.server.migration.NameSplitter.Names;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.dwp.common.mongo.test.support.DocumentMatcher.hasField;

public class PersonalDetailsMigratorTest {

    private final NameSplitter nameSplitter = mock(NameSplitter.class);
    private final PersonalDetailsMigrator underTest = new PersonalDetailsMigrator(nameSplitter);

    @Test
    public void migrateDocument() throws Exception {
        when(nameSplitter.split("Bob Builder")).thenReturn(new Names("Bob", "Builder"));
        Document original = new Document("name", "Bob Builder").append("dateOfBirth", 20101010);
        assertThat(underTest.migrate(original), allOf(
                hasField("firstName", equalTo("Bob")),
                hasField("lastName", equalTo("Builder")),
                hasField("dateOfBirth", equalTo(20101010)),
                hasField("_lastModifiedDateTime", notNullValue(Instant.class)),
                not(hasField("name"))
        ));
    }
}