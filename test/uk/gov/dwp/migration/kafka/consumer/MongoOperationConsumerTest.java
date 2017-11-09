package uk.gov.dwp.migration.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.Test;
import uk.gov.dwp.common.kafka.mongo.api.KafkaMessageWrapper;
import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MongoOperationConsumerTest {

    private final Consumer kafkaConsumer = mock(Consumer.class);
    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private final CompositeMongoOperationProcessor mongoOperationProcessor = mock(CompositeMongoOperationProcessor.class);
    private final ConsumerRecords consumerRecords = mock(ConsumerRecords.class);
    private final ConsumerRecord consumerRecord = mock(ConsumerRecord.class);
    private final KafkaMessageWrapper kafkaMessageWrapper = mock(KafkaMessageWrapper.class);
    private final MongoOperation mongoOperation = mock(MongoOperation.class);

    private final MongoOperationConsumer underTest = new MongoOperationConsumer(kafkaConsumer, objectMapper, mongoOperationProcessor);

    @Test
    public void processMongoOperation() throws Exception {
        when(kafkaConsumer.poll(1000)).thenReturn(consumerRecords);
        when(consumerRecords.iterator()).thenReturn(singletonList(consumerRecord).iterator());
        when(consumerRecord.value()).thenReturn("badger");
        when(objectMapper.readValue("badger", KafkaMessageWrapper.class)).thenReturn(kafkaMessageWrapper);
        when(kafkaMessageWrapper.getMessage()).thenReturn(mongoOperation);

        underTest.consumeRecords();

        verify(mongoOperationProcessor).process(mongoOperation);
    }
}
