package uk.gov.dwp.migration.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoOperationConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoOperationConsumer.class);

    private final Consumer<String, String> kafkaConsumer;
    private final ConsumerRecordProcessor<String, String> consumerRecordProcessor;

    public MongoOperationConsumer(Consumer<String, String> kafkaConsumer,
                                  ConsumerRecordProcessor<String, String> consumerRecordProcessor) {
        this.kafkaConsumer = kafkaConsumer;
        this.consumerRecordProcessor = consumerRecordProcessor;
    }

    public void consumerRecords() {
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(1000);
        LOGGER.debug("Received {} records", consumerRecords.count());
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            consumerRecordProcessor.process(consumerRecord);
        }
    }
}
