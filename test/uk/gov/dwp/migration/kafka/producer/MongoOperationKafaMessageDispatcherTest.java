package uk.gov.dwp.migration.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import uk.gov.dwp.migration.kafka.api.MongoOperation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MongoOperationKafaMessageDispatcherTest {

    private final KafkaProducerRecordFactory kafkaProducerRecordFactory = mock(KafkaProducerRecordFactory.class);
    private final KafkaProducer kafkaProducer = mock(KafkaProducer.class);
    private final MongoOperation mongoOperation = mock(MongoOperation.class);
    private final JsonProcessingException jsonProcessingException = mock(JsonProcessingException.class);

    private final MongoOperationKafaMessageDispatcher underTest = new MongoOperationKafaMessageDispatcher(
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
