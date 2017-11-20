package uk.gov.dwp.migration.mongo.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class KafkaMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageListener.class);

    private final ExecutorService executorService;
    private final MongoOperationConsumer mongoOperationConsumer;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public KafkaMessageListener(ExecutorService executorService,
                                MongoOperationConsumer mongoOperationConsumer) {
        this.executorService = executorService;
        this.mongoOperationConsumer = mongoOperationConsumer;
    }

    public void start() {
        LOGGER.info("Starting KafkaMessageListener");
        running.set(true);
        executorService.execute(() -> {
            while (running.get()) {
                mongoOperationConsumer.consumerRecords();
            }
        });
        LOGGER.info("Started KafkaMessageListener");
    }

    public void stop() {
        LOGGER.info("Stopping KafkaMessageListener");
        running.set(false);
        executorService.shutdown();
        LOGGER.info("Stopped KafkaMessageListener");
    }
}
