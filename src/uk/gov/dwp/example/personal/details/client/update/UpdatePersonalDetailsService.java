package uk.gov.dwp.example.personal.details.client.update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class UpdatePersonalDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePersonalDetailsService.class);
    private static final int NO_DELAY = 0;

    private final ScheduledExecutorService scheduledExecutorService;
    private final UpdatePersonalDetailsTask updatePersonalDetailsTask;
    private final Duration duration;
    private ScheduledFuture<?> future;

    public UpdatePersonalDetailsService(ScheduledExecutorService scheduledExecutorService,
                                        UpdatePersonalDetailsTask updatePersonalDetailsTask,
                                        Duration duration) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.updatePersonalDetailsTask = updatePersonalDetailsTask;
        this.duration = duration;
    }

    public void start() {
        LOGGER.info("Starting the UpdatePersonalDetailsTask");
        future = this.scheduledExecutorService.scheduleWithFixedDelay(
                updatePersonalDetailsTask,
                NO_DELAY,
                duration.getSeconds(),
                SECONDS);
        LOGGER.info("Started the UpdatePersonalDetailsTask");
    }

    public void stop() {
        LOGGER.info("Stopping the UpdatePersonalDetailsTask");
        future.cancel(true);
        LOGGER.info("Stopped the UpdatePersonalDetailsTask");
    }
}