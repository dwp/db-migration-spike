package uk.gov.dwp.example.personal.details.client.delete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.example.personal.details.client.create.CreatePersonalDetailsService;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class DeletePersonalDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePersonalDetailsService.class);
    private static final int NO_DELAY = 0;

    private final ScheduledExecutorService scheduledExecutorService;
    private final DeletePersonalDetailsTask deletePersonalDetailsTask;
    private final Duration duration;
    private ScheduledFuture<?> future;

    public DeletePersonalDetailsService(ScheduledExecutorService scheduledExecutorService,
                                        DeletePersonalDetailsTask deletePersonalDetailsTask,
                                        Duration duration) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.deletePersonalDetailsTask = deletePersonalDetailsTask;
        this.duration = duration;
    }

    public void start() {
        LOGGER.info("Starting the deletePersonalDetailsTask task");
        future = scheduledExecutorService.scheduleWithFixedDelay(
                deletePersonalDetailsTask,
                NO_DELAY,
                duration.getSeconds(),
                SECONDS);
        LOGGER.info("Started the deletePersonalDetailsTask task");
    }

    public void stop() {
        LOGGER.info("Stopping the deletePersonalDetailsTask task");
        future.cancel(true);
        LOGGER.info("Stopped the deletePersonalDetailsTask task");
    }
}
