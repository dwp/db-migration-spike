package uk.gov.dwp.migration.kafka;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import uk.gov.dwp.migration.api.InsertCommand;

public class KafkaInsertCommand<T> implements InsertCommand<T> {

    private final InsertCommand<T> delegate;
    private final Producer<String, T> producer;

    public KafkaInsertCommand(InsertCommand<T> delegate, Producer<String, T> producer) {
        this.delegate = delegate;
        this.producer = producer;
    }

    @Override
    public void insert(T document) {
        delegate.insert(document);
        producer.send(new ProducerRecord<String, T>());
    }
}
