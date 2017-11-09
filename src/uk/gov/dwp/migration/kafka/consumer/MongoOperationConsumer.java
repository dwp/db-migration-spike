package uk.gov.dwp.migration.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import uk.gov.dwp.common.kafka.mongo.api.KafkaMessageWrapper;

import java.io.IOException;

public class MongoOperationConsumer {

    private final Consumer<String, String> kafkaConsumer;
    private final ObjectMapper objectMapper;
    private final CompositeMongoOperationProcessor mongoOperationProcessor;

    public MongoOperationConsumer(Consumer<String, String> kafkaConsumer,
                                  ObjectMapper objectMapper,
                                  CompositeMongoOperationProcessor mongoOperationProcessor) {
        this.kafkaConsumer = kafkaConsumer;
        this.objectMapper = objectMapper;
        this.mongoOperationProcessor = mongoOperationProcessor;
    }

    public void consumeRecords() {
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(1000);
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            try {
                KafkaMessageWrapper kafkaMessageWrapper = objectMapper.readValue(consumerRecord.value(), KafkaMessageWrapper.class);
                mongoOperationProcessor.process(kafkaMessageWrapper.getMessage());
            } catch (IOException e) {

            }
        }
    }
}
