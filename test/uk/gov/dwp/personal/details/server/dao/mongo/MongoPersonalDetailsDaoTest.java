package uk.gov.dwp.personal.details.server.dao.mongo;

import org.bson.Document;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.dwp.common.kafka.mongo.api.MongoDeleteMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoInsertMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;
import uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage;
import uk.gov.dwp.common.mongo.test.support.DocumentMatcher;
import uk.gov.dwp.personal.details.client.PersonalDetailsMatcher;
import uk.gov.dwp.personal.details.server.dao.PersonalDetailsDao;
import uk.gov.dwp.personal.details.server.dao.mongo.support.AbstractMongoDaoTest;
import uk.gov.dwp.personal.details.server.model.PersonalDetails.PersonalDetailsBuilder;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static uk.gov.dwp.personal.details.server.dao.mongo.PersonalDetailsDocumentConverter.DATE_OF_BIRTH_FIELD;
import static uk.gov.dwp.personal.details.server.dao.mongo.PersonalDetailsDocumentConverter.NAME_FIELD;
import static uk.gov.dwp.personal.details.server.model.PersonalDetails.newPersonalDetails;
import static uk.gov.dwp.personal.details.type.PersonalDetailsId.newPersonalDetailsId;

public class MongoPersonalDetailsDaoTest extends AbstractMongoDaoTest {

    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(2010, 1, 1);
    private static final String FULL_NAME = "Bob Builder";
    private static final LocalDate NEW_DATE_OF_BIRTH = LocalDate.of(2009, 12, 31);
    private static final String NEW_FULL_NAME = "Postman Pat";

    private final PersonalDetailsId personalDetailsId = newPersonalDetailsId();

    @Autowired
    private PersonalDetailsDao underTest;

    @Test
    public void findARecordThatDoesNotExistReturnsNull() throws Exception {
        assertThat(underTest.findById(newPersonalDetailsId()), is(nullValue()));
    }

    @Test
    public void createAndRetrievePersonalDetails() throws Exception {
        underTest.create(personalDetails(personalDetailsId).build());

        assertThat(underTest.findById(personalDetailsId), is(PersonalDetailsMatcher.personalDetails()
                .withPersonalDetailsId(equalTo(personalDetailsId))
                .withName(equalTo(FULL_NAME))
                .withDateOfBirth(equalTo(DATE_OF_BIRTH))
        ));
        verify(kafkaMessageDispatcher).send(matches(mongoInsertOperation(allOf(
                DocumentMatcher.hasField(NAME_FIELD, equalTo(FULL_NAME)),
                DocumentMatcher.hasField(DATE_OF_BIRTH_FIELD, equalTo("20100101"))
        ))));
    }

    private PersonalDetailsBuilder personalDetails(PersonalDetailsId personalDetailsId) {
        return newPersonalDetails()
                .withPersonalDetailsId(personalDetailsId)
                .withName(FULL_NAME)
                .withDateOfBirth(DATE_OF_BIRTH);
    }

    @Test
    public void updateARecord() {
        underTest.create(personalDetails(personalDetailsId).build());
        reset(kafkaMessageDispatcher);

        underTest.update(personalDetails(personalDetailsId).withName(NEW_FULL_NAME).withDateOfBirth(NEW_DATE_OF_BIRTH).build());

        assertThat(underTest.findById(personalDetailsId), is(PersonalDetailsMatcher.personalDetails()
                .withPersonalDetailsId(equalTo(personalDetailsId))
                .withName(equalTo(NEW_FULL_NAME))
                .withDateOfBirth(equalTo(NEW_DATE_OF_BIRTH))
        ));
        verify(kafkaMessageDispatcher).send(matches(mongoUpdateOperation(allOf(
                DocumentMatcher.hasField("_id", equalTo(personalDetailsId.toString())),
                DocumentMatcher.hasField(NAME_FIELD, equalTo(NEW_FULL_NAME)),
                DocumentMatcher.hasField(DATE_OF_BIRTH_FIELD, equalTo("20091231"))
        ))));
    }

    @Test
    public void updateARecordThatDoesNotExist() {
        underTest.update(personalDetails(personalDetailsId).build());

        assertThat(underTest.findById(personalDetailsId), is(nullValue()));
        verifyZeroInteractions(kafkaMessageDispatcher);
    }

    @Test
    public void deleteARecordSuccessfully() {
        underTest.create(personalDetails(personalDetailsId).build());
        reset(kafkaMessageDispatcher);

        underTest.delete(personalDetailsId);

        assertThat(underTest.findById(personalDetailsId), is(nullValue()));
        verify(kafkaMessageDispatcher).send(matches(mongoDeleteOperation(
                DocumentMatcher.hasField("_id", equalTo(personalDetailsId.toString()))
        )));
    }

    @Test
    public void deleteARecordThatDoesNotExist() {
        underTest.create(personalDetails(personalDetailsId).build());
        reset(kafkaMessageDispatcher);

        underTest.delete(newPersonalDetailsId());

        assertThat(underTest.findById(personalDetailsId), is(PersonalDetailsMatcher.personalDetails()
                .withPersonalDetailsId(equalTo(personalDetailsId))
                .withName(equalTo(FULL_NAME))
                .withDateOfBirth(equalTo(DATE_OF_BIRTH))
        ));
        verifyZeroInteractions(kafkaMessageDispatcher);
    }

    @Override
    protected String getCollectionName() {
        return "personalDetails";
    }

    private MongoOperation matches(Matcher<? extends MongoOperation> mongoOperationMatcher) {
        return Mockito.argThat(new HamcrestArgumentMatcher<>(mongoOperationMatcher));
    }

    private MongoOperationMatcher<MongoInsertMessage> mongoInsertOperation(Matcher<Document> dbObjectMatcher) {
        return new MongoOperationMatcher<>(
                mongoDaoProperties.getDbName(),
                mongoDaoProperties.getPersonalDetails().getName(),
                dbObjectMatcher
        );
    }

    private MongoOperationMatcher<MongoUpdateMessage> mongoUpdateOperation(Matcher<Document> dbObjectMatcher) {
        return new MongoOperationMatcher<>(
                mongoDaoProperties.getDbName(),
                mongoDaoProperties.getPersonalDetails().getName(),
                dbObjectMatcher);
    }

    private MongoOperationMatcher<MongoDeleteMessage> mongoDeleteOperation(Matcher<Document> dbObjectMatcher) {
        return new MongoOperationMatcher<>(
                mongoDaoProperties.getDbName(),
                mongoDaoProperties.getPersonalDetails().getName(),
                dbObjectMatcher);
    }

    public static class MongoOperationMatcher<T extends MongoOperation> extends TypeSafeDiagnosingMatcher<T> {

        private String dbName;
        private String collectionName;
        private Matcher<Document> documentMatcher;

        MongoOperationMatcher(String dbName,
                              String collectionName,
                              Matcher<Document> dbObjectMatcher) {
            this.dbName = dbName;
            this.collectionName = collectionName;
            this.documentMatcher = dbObjectMatcher;
        }

        @Override
        protected boolean matchesSafely(T mongoOperation, Description description) {
            return dbName.equals(mongoOperation.getDb()) &&
                    collectionName.equals(mongoOperation.getCollection()) &&
                    documentMatcher.matches(mongoOperation.getData());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("db=").appendText(dbName)
                    .appendText(", collection=").appendText(collectionName)
                    .appendText(", dbObject=").appendDescriptionOf(documentMatcher);
        }
    }
}
