package uk.gov.dwp.example.personal.details.client.delete;

import org.junit.Test;
import uk.gov.dwp.personal.details.client.v2.PersonalDetailsV2Client;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class DeletePersonalDetailsTaskTest {

    private static final PersonalDetailsId PERSONAL_DETAILS_ID = PersonalDetailsId.newPersonalDetailsId();

    private final List<PersonalDetailsId> personalDetailsIdRegistry = mock(List.class);
    private final PersonalDetailsV2Client personalDetailsClient = mock(PersonalDetailsV2Client.class);

    @Test
    public void updateWhenPersonalDetailsWhenListIsEmpty() {
        new DeletePersonalDetailsTask(personalDetailsClient, personalDetailsIdRegistry, Optional::empty)
                .run();

        verifyZeroInteractions(personalDetailsClient);
        verifyZeroInteractions(personalDetailsIdRegistry);
    }

    @Test
    public void updatePersonalDetailsWhenListIsNotEmpty() {

        new DeletePersonalDetailsTask(personalDetailsClient, personalDetailsIdRegistry, () -> Optional.of(PERSONAL_DETAILS_ID))
                .run();

        verify(personalDetailsClient).delete(PERSONAL_DETAILS_ID);
        verify(personalDetailsIdRegistry).remove(PERSONAL_DETAILS_ID);
    }
}