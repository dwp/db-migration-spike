package uk.gov.dwp.migration.mongo.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerRecordProcessor<K, V> {

    void process(ConsumerRecord<K, V> consumerRecord);
}
