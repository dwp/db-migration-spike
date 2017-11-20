package uk.gov.dwp.migration.mongo.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import uk.gov.dwp.common.kafka.mongo.api.KafkaMessageWrapper;
import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;
import uk.gov.dwp.migration.mongo.kafka.api.MongoOperationProcessor;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ConsumerRecordAdapterProcessorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private MongoOperationProcessor<MongoOperation> mongoOperationProcessor;
    @Mock
    private ConsumerRecord<String, String> consumerRecord;
    @Mock
    private KafkaMessageWrapper<MongoOperation> kafkaMessageWrapper;
    @Mock
    private MongoOperation mongoOperation;
    @InjectMocks
    private ConsumerRecordAdapterProcessor underTest;

    @Test
    public void successfullyProcessConsumerRecord() throws Exception {
        when(consumerRecord.value()).thenReturn("badger");
        when(objectMapper.readValue("badger", KafkaMessageWrapper.class)).thenReturn(kafkaMessageWrapper);
        when(kafkaMessageWrapper.getMessage()).thenReturn(mongoOperation);

        underTest.process(consumerRecord);

        verify(mongoOperationProcessor).process(mongoOperation);
    }

    @Test
    public void desearialisingConsumerRecordThrowsException() throws Exception {
        when(consumerRecord.value()).thenReturn("badger");
        when(objectMapper.readValue("badger", KafkaMessageWrapper.class)).thenThrow(IOException.class);

        underTest.process(consumerRecord);

        verifyZeroInteractions(mongoOperationProcessor);
    }
}
