package uk.gov.dwp.personal.details.server.resource.v2;

import org.junit.After;
import org.junit.Test;
import uk.gov.dwp.personal.details.client.v2.CreatePersonalDetailsV2Request;
import uk.gov.dwp.personal.details.client.v2.PersonalDetailsV2Response;
import uk.gov.dwp.personal.details.client.v2.UpdatePersonalDetailsV2Request;
import uk.gov.dwp.personal.details.server.dao.PersonalDetailsDao;
import uk.gov.dwp.personal.details.server.model.PersonalDetails;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class PersonalDetailsV2ResourceTest {

    private static final PersonalDetailsId PERSONAL_DETAILS_ID = PersonalDetailsId.newPersonalDetailsId();
    private final PersonalDetailsDao personalDetailsDao = mock(PersonalDetailsDao.class);
    private final PersonalDetails personalDetails = mock(PersonalDetails.class);
    private final PersonalDetailsV2ResponseAdapter personalDetailsV2ResponseAdapter = mock(PersonalDetailsV2ResponseAdapter.class);
    private final CreatePersonalDetailsV2RequestAdapter createPersonalDetailsV2RequestAdapter = mock(CreatePersonalDetailsV2RequestAdapter.class);
    private final UpdatePersonalDetailsV2RequestAdapter updatePersonalDetailsV2RequestAdapter = mock(UpdatePersonalDetailsV2RequestAdapter.class);

    private final PersonalDetailsV2Resource underTest = new PersonalDetailsV2Resource(
            personalDetailsDao,
            personalDetailsV2ResponseAdapter,
            createPersonalDetailsV2RequestAdapter,
            updatePersonalDetailsV2RequestAdapter
    );

    @After
    public void tearDown() {
        verifyNoMoreInteractions(personalDetailsDao);
    }

    @Test
    public void findByIdDelegatesToDao() throws Exception {
        final PersonalDetailsV2Response personalDetailsResponse = mock(PersonalDetailsV2Response.class);
        when(personalDetailsDao.findById(PERSONAL_DETAILS_ID)).thenReturn(personalDetails);
        when(personalDetailsV2ResponseAdapter.toPersonalDetails(personalDetails)).thenReturn(personalDetailsResponse);

        assertThat(underTest.findById(PERSONAL_DETAILS_ID), is(personalDetailsResponse));

        verify(personalDetailsDao).findById(PERSONAL_DETAILS_ID);
    }

    @Test
    public void createPersonalDetailsDelegatesToDao() throws Exception {
        CreatePersonalDetailsV2Request createPersonalDetailsRequest = mock(CreatePersonalDetailsV2Request.class);
        when(createPersonalDetailsV2RequestAdapter.toPersonalDetails(createPersonalDetailsRequest))
                .thenReturn(personalDetails);

        underTest.create(createPersonalDetailsRequest);

        verify(personalDetailsDao).create(personalDetails);
    }

    @Test
    public void updatePersonalDetailsDelegatesToDao() throws Exception {
        UpdatePersonalDetailsV2Request updatePersonalDetailsRequest = mock(UpdatePersonalDetailsV2Request.class);
        when(updatePersonalDetailsV2RequestAdapter.toPersonalDetails(updatePersonalDetailsRequest))
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
