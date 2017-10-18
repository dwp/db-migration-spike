package uk.gov.dwp.personal.details.client.find;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.personal.details.client.create.CreatePersonalDetailsService;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class FindPersonalDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePersonalDetailsService.class);
    private static final int NO_DELAY = 0;

    private final ScheduledExecutorService scheduledExecutorService;
    private final FindPersonalDetailsTask findPersonalDetailsTask;
    private final Duration duration;
    private ScheduledFuture<?> future;

    public FindPersonalDetailsService(ScheduledExecutorService scheduledExecutorService,
                                      FindPersonalDetailsTask findPersonalDetailsTask,
                                      Duration duration) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.findPersonalDetailsTask = findPersonalDetailsTask;
        this.duration = duration;
    }

    public void start() {
        LOGGER.info("Starting the findPersonalDetailsTask task");
        future = scheduledExecutorService.scheduleWithFixedDelay(
                findPersonalDetailsTask,
                NO_DELAY,
                duration.getSeconds(),
                SECONDS);
        LOGGER.info("Started the findPersonalDetailsTask task");
    }

    public void stop() {
        LOGGER.info("Stopping the findPersonalDetailsTask task");
        future.cancel(true);
        LOGGER.info("Stopped the findPersonalDetailsTask task");
    }
}
