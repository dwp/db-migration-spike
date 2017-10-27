package uk.gov.dwp.example.personal.details.client.create;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.personal.details.client.PersonalDetailsClient;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.dwp.example.personal.details.client.RandomCreatePersonalDetailsRequestBuilder.newRandomCreatePersonalDetailsRequest;

public class CreatePersonalDetailsTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePersonalDetailsTask.class);

    private final PersonalDetailsClient personalDetailsClient;
    private final List<PersonalDetailsId> personalDetailsIdRegistry;

    public CreatePersonalDetailsTask(PersonalDetailsClient personalDetailsClient,
                                     ArrayList<PersonalDetailsId> personalDetailsIdRegistry) {
        this.personalDetailsClient = personalDetailsClient;
        this.personalDetailsIdRegistry = personalDetailsIdRegistry;
    }

    @Override
    public void run() {
        try {
            PersonalDetailsId personalDetailsId = PersonalDetailsId.newPersonalDetailsId();
            personalDetailsClient.create(newRandomCreatePersonalDetailsRequest().withPersonalDetailsId(personalDetailsId).build());
            LOGGER.debug("Created personalDetails with id: {}", personalDetailsId);
            personalDetailsIdRegistry.add(personalDetailsId);
        } catch (Exception e) {
            LOGGER.error("Could not create PersonalDetails", e);
        }
    }
}
