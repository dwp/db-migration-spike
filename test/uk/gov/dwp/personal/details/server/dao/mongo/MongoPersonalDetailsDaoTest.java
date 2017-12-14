package uk.gov.dwp.personal.details.server.dao.mongo;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;
import uk.gov.dwp.common.kafka.mongo.api.test.support.MongoOperationMatcher;
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
import static uk.gov.dwp.common.kafka.mongo.api.test.support.MongoOperationMatcher.mongoDeleteOperation;
import static uk.gov.dwp.common.kafka.mongo.api.test.support.MongoOperationMatcher.mongoInsertOperation;
import static uk.gov.dwp.common.kafka.mongo.api.test.support.MongoOperationMatcher.mongoUpdateOperation;
import static uk.gov.dwp.personal.details.server.dao.mongo.PersonalDetailsDocumentConverter.DATE_OF_BIRTH_FIELD;
import static uk.gov.dwp.personal.details.server.dao.mongo.PersonalDetailsDocumentConverter.FIRST_NAME_FIELD;
import static uk.gov.dwp.personal.details.server.dao.mongo.PersonalDetailsDocumentConverter.LAST_NAME_FIELD;
import static uk.gov.dwp.personal.details.server.model.PersonalDetails.newPersonalDetails;
import static uk.gov.dwp.personal.details.type.PersonalDetailsId.newPersonalDetailsId;

public class MongoPersonalDetailsDaoTest extends AbstractMongoDaoTest {

    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(2010, 1, 1);
    private static final String FIRST_NAME = "Bob";
    private static final String LAST_NAME = "Builder";
    private static final LocalDate NEW_DATE_OF_BIRTH = LocalDate.of(2009, 12, 31);
    private static final String NEW_FIRST_NAME = "Postman";
    private static final String NEW_LAST_NAME = "Pat";

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
                .withFirstName(equalTo(FIRST_NAME))
                .withLastName(equalTo(LAST_NAME))
                .withDateOfBirth(equalTo(DATE_OF_BIRTH))
        ));
        verify(kafkaMessageDispatcher).send(matches(mongoInsertOperation(allOf(
                DocumentMatcher.hasField(FIRST_NAME_FIELD, equalTo(FIRST_NAME)),
                DocumentMatcher.hasField(LAST_NAME_FIELD, equalTo(LAST_NAME)),
                DocumentMatcher.hasField(DATE_OF_BIRTH_FIELD, equalTo("20100101"))
        ))));
    }

    private PersonalDetailsBuilder personalDetails(PersonalDetailsId personalDetailsId) {
        return newPersonalDetails()
                .withPersonalDetailsId(personalDetailsId)
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withDateOfBirth(DATE_OF_BIRTH);
    }

    @Test
    public void updateARecord() {
        underTest.create(personalDetails(personalDetailsId).build());
        reset(kafkaMessageDispatcher);

        underTest.update(personalDetails(personalDetailsId)
                .withFirstName(NEW_FIRST_NAME)
                .withLastName(NEW_LAST_NAME)
                .withDateOfBirth(NEW_DATE_OF_BIRTH)
                .build());

        assertThat(underTest.findById(personalDetailsId), is(PersonalDetailsMatcher.personalDetails()
                .withPersonalDetailsId(equalTo(personalDetailsId))
                .withFirstName(equalTo(NEW_FIRST_NAME))
                .withLastName(equalTo(NEW_LAST_NAME))
                .withDateOfBirth(equalTo(NEW_DATE_OF_BIRTH))
        ));
        verify(kafkaMessageDispatcher).send(matches(mongoUpdateOperation(allOf(
                DocumentMatcher.hasField("_id", equalTo(personalDetailsId.toString())),
                DocumentMatcher.hasField(FIRST_NAME_FIELD, equalTo(NEW_FIRST_NAME)),
                DocumentMatcher.hasField(LAST_NAME_FIELD, equalTo(NEW_LAST_NAME)),
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
                .withFirstName(equalTo(FIRST_NAME))
                .withLastName(equalTo(LAST_NAME))
                .withDateOfBirth(equalTo(DATE_OF_BIRTH))
        ));
        verifyZeroInteractions(kafkaMessageDispatcher);
    }

    @Override
    protected String getCollectionName() {
        return "personalDetailsV2";
    }

    private MongoOperation matches(MongoOperationMatcher<? extends MongoOperation> mongoOperationMatcher) {
        return Mockito.argThat(new HamcrestArgumentMatcher<>(mongoOperationMatcher
                .withDbName(mongoDaoProperties.getDbName())
                .withCollectionName(getCollectionName())
        ));
    }
}
