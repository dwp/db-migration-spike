package uk.gov.dwp.personal.details.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.api.PersonalDetailsClient;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PersonalDetailsCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalDetailsCreator.class);

    private final PersonalDetailsClient personalDetailsClient;
    private final ScheduledExecutorService scheduledExecutorService;

    public PersonalDetailsCreator(PersonalDetailsClient personalDetailsClient,
                                  ScheduledExecutorService scheduledExecutorService) {
        this.personalDetailsClient = personalDetailsClient;
        this.scheduledExecutorService = scheduledExecutorService;
        ScheduledFuture<?> future = this.scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                personalDetailsClient.create(new RandomPersonalDetailsBuilder().build());
            } catch (Exception e) {
                LOGGER.error("Could not create PersonalDetails", e);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}