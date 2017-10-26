package uk.gov.dwp.personal.details.server.resource;

import org.junit.After;
import org.junit.Test;
import uk.gov.dwp.personal.details.client.CreatePersonalDetailsRequest;
import uk.gov.dwp.personal.details.client.PersonalDetailsResponse;
import uk.gov.dwp.personal.details.client.UpdatePersonalDetailsRequest;
import uk.gov.dwp.personal.details.server.dao.PersonalDetailsDao;
import uk.gov.dwp.personal.details.server.model.PersonalDetails;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

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
    private final PersonalDetailsResponseAdapter personalDetailsResponseAdapter = mock(PersonalDetailsResponseAdapter.class);
    private final CreatePersonalDetailsRequestAdapter createPersonalDetailsRequestAdapter = mock(CreatePersonalDetailsRequestAdapter.class);
    private final UpdatePersonalDetailsRequestAdapter updatePersonalDetailsRequestAdapter = mock(UpdatePersonalDetailsRequestAdapter.class);

    private final PersonalDetailsResource underTest = new PersonalDetailsResource(
            personalDetailsDao,
            personalDetailsResponseAdapter,
            createPersonalDetailsRequestAdapter,
            updatePersonalDetailsRequestAdapter
    );

    @After
    public void tearDown() {
        verifyNoMoreInteractions(personalDetailsDao);
    }

    @Test
    public void findByIdDelegatesToDao() throws Exception {
        final PersonalDetailsResponse personalDetailsResponse = mock(PersonalDetailsResponse.class);
        when(personalDetailsDao.findById(PERSONAL_DETAILS_ID)).thenReturn(personalDetails);
        when(personalDetailsResponseAdapter.toPersonalDetails(personalDetails)).thenReturn(personalDetailsResponse);

        assertThat(underTest.findById(PERSONAL_DETAILS_ID), is(personalDetailsResponse));

        verify(personalDetailsDao).findById(PERSONAL_DETAILS_ID);
    }

    @Test
    public void createPersonalDetailsDelegatesToDao() throws Exception {
        CreatePersonalDetailsRequest createPersonalDetailsRequest = mock(CreatePersonalDetailsRequest.class);
        when(createPersonalDetailsRequestAdapter.toPersonalDetails(createPersonalDetailsRequest))
                .thenReturn(personalDetails);

        underTest.create(createPersonalDetailsRequest);

        verify(personalDetailsDao).create(personalDetails);
    }

    @Test
    public void updatePersonalDetailsDelegatesToDao() throws Exception {
        UpdatePersonalDetailsRequest updatePersonalDetailsRequest = mock(UpdatePersonalDetailsRequest.class);
        when(updatePersonalDetailsRequestAdapter.toPersonalDetails(updatePersonalDetailsRequest))
                .thenReturn(personalDetails);

        underTest.update(updatePersonalDetailsRequest);

        verify(personalDetailsDao).update(personalDetails);
    }

    @Test
    public void deletePersonalDetailsDelegatesToDao() throws Exception {
        underTest.delete(PERSONAL_DETAILS_ID);

        verify(personalDetailsDao).delete(PERSONAL_DETAILS_ID);
    }
}
