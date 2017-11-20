package uk.gov.dwp.migration.mongo.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.common.kafka.mongo.api.KafkaMessageWrapper;
import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;
import uk.gov.dwp.migration.mongo.kafka.api.MongoOperationProcessor;

import java.io.IOException;

public class ConsumerRecordAdapterProcessor implements ConsumerRecordProcessor<String, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerRecordAdapterProcessor.class);

    private final ObjectMapper objectMapper;
    private final MongoOperationProcessor<MongoOperation> mongoOperationProcessor;

    public ConsumerRecordAdapterProcessor(ObjectMapper objectMapper,
                                          MongoOperationProcessor<MongoOperation> mongoOperationProcessor) {
        this.objectMapper = objectMapper;
        this.mongoOperationProcessor = mongoOperationProcessor;
    }

    @Override
    public void process(ConsumerRecord<String, String> consumerRecord) {
        try {
            KafkaMessageWrapper kafkaMessageWrapper = objectMapper.readValue(consumerRecord.value(), KafkaMessageWrapper.class);
            mongoOperationProcessor.process(kafkaMessageWrapper.getMessage());
        } catch (IOException e) {
            LOGGER.error("Could not deserialise '{}'", consumerRecord.value(), e);
        }
    }
}
