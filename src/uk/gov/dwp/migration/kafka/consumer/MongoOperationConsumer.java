package uk.gov.dwp.migration.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.common.kafka.mongo.api.KafkaMessageWrapper;
import uk.gov.dwp.migration.kafka.api.MongoOperationProcessor;

import java.io.IOException;

public class MongoOperationConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoOperationConsumer.class);

    private final Consumer<String, String> kafkaConsumer;
    private final ObjectMapper objectMapper;
    private final MongoOperationProcessor mongoOperationProcessor;

    public MongoOperationConsumer(Consumer<String, String> kafkaConsumer,
                                  ObjectMapper objectMapper,
                                  MongoOperationProcessor mongoOperationProcessor) {
        this.kafkaConsumer = kafkaConsumer;
        this.objectMapper = objectMapper;
        this.mongoOperationProcessor = mongoOperationProcessor;
    }

    public void consumeRecords() {
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(1000);
        LOGGER.debug("Received {} records", consumerRecords.count());
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            try {
                KafkaMessageWrapper kafkaMessageWrapper = objectMapper.readValue(consumerRecord.value(), KafkaMessageWrapper.class);
                mongoOperationProcessor.process(kafkaMessageWrapper.getMessage());
            } catch (IOException e) {

            }
        }
    }
}
