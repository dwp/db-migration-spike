package uk.gov.dwp.example.personal.details.client.create;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class CreatePersonalDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePersonalDetailsService.class);
    private static final int NO_DELAY = 0;

    private final ScheduledExecutorService scheduledExecutorService;
    private final CreatePersonalDetailsTask personalDetailsCreatorTask;
    private final Duration duration;
    private ScheduledFuture<?> future;

    public CreatePersonalDetailsService(ScheduledExecutorService scheduledExecutorService,
                                        CreatePersonalDetailsTask personalDetailsCreatorTask,
                                        Duration duration) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.personalDetailsCreatorTask = personalDetailsCreatorTask;
        this.duration = duration;
    }

    public void start() {
        LOGGER.info("Starting the personalDetailsCreator task");
        future = this.scheduledExecutorService.scheduleWithFixedDelay(
                personalDetailsCreatorTask,
                NO_DELAY,
                duration.getSeconds(),
                SECONDS);
        LOGGER.info("Started the personalDetailsCreator task");
    }

    public void stop() {
        LOGGER.info("Stopping the personalDetailsCreator task");
        future.cancel(true);
        LOGGER.info("Stopped the personalDetailsCreator task");
    }
}