package uk.gov.dwp.migration.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import uk.gov.dwp.migration.kafka.api.KafkaMessageWrapper;
import uk.gov.dwp.migration.kafka.api.MongoOperation;

import java.time.Instant;

import static java.util.UUID.randomUUID;
import static uk.gov.dwp.migration.kafka.api.TraceId.newTraceId;

public class KafkaProducerRecordFactory {

    private final ObjectMapper objectMapper;
    private final String topic;

    public KafkaProducerRecordFactory(ObjectMapper objectMapper,
                                      String topic) {
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    public ProducerRecord<String, String> create(MongoOperation mongoOperation) throws JsonProcessingException {
        return new ProducerRecord<>(topic, null, deserialiseMongoOperation(mongoOperation));
    }

    private String deserialiseMongoOperation(MongoOperation mongoOperation) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new KafkaMessageWrapper<>(
                "V2",
                newTraceId(),
                randomUUID(),
                Instant.now(),
                "1.0", // TODO: Should this reflect the version of universe?
                mongoOperation));
    }
}
