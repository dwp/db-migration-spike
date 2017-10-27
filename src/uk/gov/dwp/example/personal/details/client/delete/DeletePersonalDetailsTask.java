package uk.gov.dwp.example.personal.details.client.delete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.personal.details.client.PersonalDetailsClient;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class DeletePersonalDetailsTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeletePersonalDetailsTask.class);

    private final PersonalDetailsClient personalDetailsClient;
    private final List<PersonalDetailsId> personalDetailsIdRegistry;
    private final Supplier<Optional<PersonalDetailsId>> personalDetailsIdSupplier;

    public DeletePersonalDetailsTask(PersonalDetailsClient personalDetailsClient,
                                     List<PersonalDetailsId> personalDetailsIdRegistry,
                                     Supplier<Optional<PersonalDetailsId>> personalDetailsIdSupplier) {
        this.personalDetailsClient = personalDetailsClient;
        this.personalDetailsIdRegistry = personalDetailsIdRegistry;
        this.personalDetailsIdSupplier = personalDetailsIdSupplier;
    }

    @Override
    public void run() {
        try {
            personalDetailsIdSupplier.get().ifPresent(personalDetailsId -> {
                personalDetailsClient.delete(personalDetailsId);
                personalDetailsIdRegistry.remove(personalDetailsId);
                LOGGER.debug("Deleted PersonalDetails: {}", personalDetailsId);
            });
        } catch (Exception e) {
            LOGGER.error("Could not delete PersonalDetails", e);
        }
    }

}
