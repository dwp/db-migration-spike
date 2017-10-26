package uk.gov.dwp.personal.details.server.dao.mongo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.dwp.personal.details.client.PersonalDetailsMatcher;
import uk.gov.dwp.personal.details.server.dao.PersonalDetailsDao;
import uk.gov.dwp.personal.details.server.dao.mongo.support.AbstractMongoDaoTest;
import uk.gov.dwp.personal.details.server.model.PersonalDetails.PersonalDetailsBuilder;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
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

        underTest.update(personalDetails(personalDetailsId).withName(NEW_FULL_NAME).withDateOfBirth(NEW_DATE_OF_BIRTH).build());

        assertThat(underTest.findById(personalDetailsId), is(PersonalDetailsMatcher.personalDetails()
                .withPersonalDetailsId(equalTo(personalDetailsId))
                .withName(equalTo(NEW_FULL_NAME))
                .withDateOfBirth(equalTo(NEW_DATE_OF_BIRTH))
        ));
    }

    @Test
    public void updateARecordThatDoesNotExist() {
        underTest.update(personalDetails(personalDetailsId).build());

        assertThat(underTest.findById(personalDetailsId), is(nullValue()));
    }

    @Test
    public void deleteARecordSuccessfully() {
        underTest.create(personalDetails(personalDetailsId).build());

        underTest.delete(personalDetailsId);

        assertThat(underTest.findById(personalDetailsId), is(nullValue()));
    }

    @Test
    public void deleteARecordThatDoesNotExist() {
        underTest.create(personalDetails(personalDetailsId).build());

        underTest.delete(newPersonalDetailsId());

        assertThat(underTest.findById(personalDetailsId), is(PersonalDetailsMatcher.personalDetails()
                .withPersonalDetailsId(equalTo(personalDetailsId))
                .withName(equalTo(FULL_NAME))
                .withDateOfBirth(equalTo(DATE_OF_BIRTH))
        ));
    }

    @Override
    protected String getCollectionName() {
        return "personalDetails";
    }
}
