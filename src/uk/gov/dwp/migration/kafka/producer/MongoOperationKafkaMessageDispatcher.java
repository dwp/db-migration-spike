package uk.gov.dwp.migration.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.migration.kafka.api.MongoOperation;

public class MongoOperationKafkaMessageDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoOperationKafkaMessageDispatcher.class);
    private static final Logger KAFKA_ERROR_LOG = LoggerFactory.getLogger("KAFKA_ERROR_LOG");

    private final Producer<String, String> kafkaProducer;
    private final KafkaProducerRecordFactory kafkaProducerRecordFactory;

    public MongoOperationKafkaMessageDispatcher(Producer<String, String> kafkaProducer,
                                                KafkaProducerRecordFactory kafkaProducerRecordFactory) {
        this.kafkaProducer = kafkaProducer;
        this.kafkaProducerRecordFactory = kafkaProducerRecordFactory;
    }

    public void send(MongoOperation mongoOperation) {
        try {
            ProducerRecord<String, String> producerRecord = kafkaProducerRecordFactory.create(mongoOperation);
            try {
                kafkaProducer.send(producerRecord);
            } catch (Exception e) {
                LOGGER.error("Unable to send message", e);
                KAFKA_ERROR_LOG.error(
                        "db={}, collection={}, message={}",
                        mongoOperation.getDb(),
                        mongoOperation.getCollection(),
                        producerRecord.value());
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Unable to create ProducerRecord for: {}", mongoOperation, e);
        }
    }
}
