package uk.gov.dwp.migration.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.concurrent.atomic.AtomicBoolean;

public class KafkaMongoOperationConsumer {

    private final KafkaConsumer<String, KafkaMongoOperation> kafkaConsumer;
    private AtomicBoolean running = new AtomicBoolean(false);

    public KafkaMongoOperationConsumer(KafkaConsumer<String, KafkaMongoOperation> kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    public void start() {
        running.set(true);
        while (running.get()) {
            ConsumerRecords<String, KafkaMongoOperation> consumerRecords = kafkaConsumer.poll(1000);
            for (ConsumerRecord<String, KafkaMongoOperation> consumerRecord : consumerRecords) {
                consumerRecord.value();
                // TODO: Based upon the operation INSERT/UPDATE/DELETE call the relevant command
            }
        }
    }

    public void stop() {
        running.set(false);
    }


}
