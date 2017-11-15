package uk.gov.dwp.migration.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MongoOperationConsumerTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private Consumer kafkaConsumer;
    @Mock
    private ConsumerRecords consumerRecords;
    @Mock
    private ConsumerRecord consumerRecord;
    @Mock
    private ConsumerRecordProcessor<String, String> consumerRecordProcessor;
    @InjectMocks
    private MongoOperationConsumer underTest;

    @Test
    public void processMongoOperation() throws Exception {
        when(kafkaConsumer.poll(1000)).thenReturn(consumerRecords);
        when(consumerRecords.iterator()).thenReturn(singletonList(consumerRecord).iterator());

        underTest.consumerRecords();

        verify(consumerRecordProcessor).process(consumerRecord);
    }
}
