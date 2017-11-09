package uk.gov.dwp.migration.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;
import uk.gov.dwp.common.kafka.mongo.producer.KafkaProducerRecordFactory;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MongoOperationKafkaMessageDispatcherTest {

    private final KafkaProducerRecordFactory kafkaProducerRecordFactory = mock(KafkaProducerRecordFactory.class);
    private final KafkaProducer kafkaProducer = mock(KafkaProducer.class);
    private final MongoOperation mongoOperation = mock(MongoOperation.class);
    private final JsonProcessingException jsonProcessingException = mock(JsonProcessingException.class);

    private final MongoOperationKafkaMessageDispatcher underTest = new MongoOperationKafkaMessageDispatcher(
            kafkaProducer,
            kafkaProducerRecordFactory
    );

    @Test
    public void messageIsNotSentIfMongoOperationCannotBeSerialised() throws Exception {
        when(kafkaProducerRecordFactory.create(mongoOperation)).thenThrow(jsonProcessingException);

        underTest.send(mongoOperation);

        verifyZeroInteractions(kafkaProducer);
    }

    @Test
    public void successfullySendKafkaMessage() throws JsonProcessingException {
        ProducerRecord producerRecord = mock(ProducerRecord.class);
        when(kafkaProducerRecordFactory.create(mongoOperation)).thenReturn(producerRecord);

        underTest.send(mongoOperation);

        verify(kafkaProducer).send(producerRecord);
    }
}
