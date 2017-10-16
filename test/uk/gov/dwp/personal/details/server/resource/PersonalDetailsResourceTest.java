package uk.gov.dwp.personal.details.server.resource;

import org.junit.After;
import org.junit.Test;
import uk.gov.dwp.api.PersonalDetails;
import uk.gov.dwp.api.PersonalDetailsId;
import uk.gov.dwp.personal.details.server.dao.PersonalDetailsDao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class PersonalDetailsResourceTest {

    private static final PersonalDetailsId PERSONAL_DETAILS_ID = PersonalDetailsId.newPersonalDetailsId();
    private final PersonalDetailsDao personalDetailsDao = mock(PersonalDetailsDao.class);
    private final PersonalDetails personalDetails = mock(PersonalDetails.class);

    private final PersonalDetailsResource underTest = new PersonalDetailsResource(personalDetailsDao);

    @After
    public void tearDown() {
        verifyNoMoreInteractions(personalDetailsDao);
    }

    @Test
    public void findByIdDelegatesToDao() throws Exception {
        when(personalDetailsDao.findById(PERSONAL_DETAILS_ID)).thenReturn(personalDetails);

        assertThat(underTest.findById(PERSONAL_DETAILS_ID), is(personalDetails));

        verify(personalDetailsDao).findById(PERSONAL_DETAILS_ID);
    }

    @Test
    public void createPersonalDetailsDelegatesToDao() throws Exception {
        underTest.create(personalDetails);

        verify(personalDetailsDao).create(personalDetails);
    }

    @Test
    public void updatePersonalDetailsDelegatesToDao() throws Exception {
        underTest.update(personalDetails);

        verify(personalDetailsDao).update(personalDetails);
    }

    @Test
    public void deletePersonalDetailsDelegatesToDao() throws Exception {
        underTest.delete(PERSONAL_DETAILS_ID);

        verify(personalDetailsDao).delete(PERSONAL_DETAILS_ID);
    }
}
