package uk.gov.dwp.example.personal.details.client.find;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.personal.details.client.v2.PersonalDetailsV2Client;
import uk.gov.dwp.personal.details.client.v2.PersonalDetailsV2Response;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.util.Optional;
import java.util.function.Supplier;

public class FindPersonalDetailsTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FindPersonalDetailsTask.class);

    private final PersonalDetailsV2Client personalDetailsClient;
    private final Supplier<Optional<PersonalDetailsId>> personalDetailsIdSupplier;

    public FindPersonalDetailsTask(PersonalDetailsV2Client personalDetailsClient,
                                   Supplier<Optional<PersonalDetailsId>> personalDetailsIdSupplier) {
        this.personalDetailsClient = personalDetailsClient;
        this.personalDetailsIdSupplier = personalDetailsIdSupplier;
    }

    @Override
    public void run() {
        try {
            personalDetailsIdSupplier.get().ifPresent(personalDetailsId -> {
                PersonalDetailsV2Response personalDetails = personalDetailsClient.findById(personalDetailsId);
                if (personalDetails != null) {
                    LOGGER.debug("Found PersonalDetails: {}", personalDetails.getPersonalDetailsId());
                } else {
                    LOGGER.warn("Could not find personalDetails: {}", personalDetailsId);
                }
            });
        } catch (Exception e) {
            LOGGER.error("Could not create PersonalDetails", e);
        }
    }

}
