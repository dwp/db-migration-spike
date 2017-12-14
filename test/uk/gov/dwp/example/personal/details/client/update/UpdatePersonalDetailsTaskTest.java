package uk.gov.dwp.example.personal.details.client.update;

import org.junit.Test;
import uk.gov.dwp.example.personal.details.client.RandomPersonalDetailsGenerator;
import uk.gov.dwp.personal.details.client.v2.PersonalDetailsV2Client;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.dwp.personal.details.client.UpdatePersonalDetailsRequest.newUpdatePersonalDetailsRequest;

public class UpdatePersonalDetailsTaskTest {

    private static final PersonalDetailsId PERSONAL_DETAILS_ID = PersonalDetailsId.newPersonalDetailsId();
    private final PersonalDetailsV2Client personalDetailsClient = mock(PersonalDetailsV2Client.class);
    private final RandomPersonalDetailsGenerator personalDetailsGenerator = mock(RandomPersonalDetailsGenerator.class);

    @Test
    public void updateWhenPersonalDetailsWhenListIsEmpty() {
        new UpdatePersonalDetailsTask(personalDetailsClient, Optional::empty, personalDetailsGenerator).run();

        verifyZeroInteractions(personalDetailsClient);
    }

    @Test
    public void updatePersonalDetailsWhenListIsNotEmpty() {
        when(personalDetailsGenerator.randomDateOfBirth()).thenReturn(LocalDate.now());
        when(personalDetailsGenerator.randomFirstName()).thenReturn("Bob");
        when(personalDetailsGenerator.randomLastName()).thenReturn("Builder");

        new UpdatePersonalDetailsTask(personalDetailsClient, () -> Optional.of(PERSONAL_DETAILS_ID), personalDetailsGenerator).run();

        verify(personalDetailsClient).update(refEq(newUpdatePersonalDetailsRequest()
                .withPersonalDetailsId(PERSONAL_DETAILS_ID)
                .withFirstName("Bob")
                .withLastName("Builder")
                .withDateOfBirth(LocalDate.now())
                .build())
        );
    }
}