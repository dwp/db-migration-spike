package uk.gov.dwp.migration.mongo.kafka.consumer.configuration;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.common.jackson.spring.JacksonConfiguration;
import uk.gov.dwp.common.kafka.configuration.KafkaProperties;
import uk.gov.dwp.migration.mongo.configuration.DestinationMigrationDaoProperties;
import uk.gov.dwp.migration.mongo.configuration.SourceMigrationDaoProperties;
import uk.gov.dwp.migration.mongo.kafka.consumer.ConsumerRecordAdapterProcessor;
import uk.gov.dwp.migration.mongo.kafka.consumer.ConsumerRecordProcessor;
import uk.gov.dwp.migration.mongo.kafka.consumer.KafkaMessageListener;
import uk.gov.dwp.migration.mongo.kafka.consumer.MongoOperationConsumer;
import uk.gov.dwp.migration.mongo.kafka.consumer.MongoOperationDelegatingProcessorRegistry;

import java.util.Collections;
import java.util.concurrent.Executors;

@Configuration
@Import({
        JacksonConfiguration.class,
})
@EnableConfigurationProperties({
        KafkaProperties.class,
        DestinationMigrationDaoProperties.class
})
public class KafkaConsumerConfiguration {

    @Bean
    public Consumer<String, String> kafkaConsumer(SourceMigrationDaoProperties sourceMigrationDaoProperties) {
        KafkaProperties kafkaProperties = sourceMigrationDaoProperties.getKafkaConsumer();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(kafkaProperties.toKafkaConsumerProperties());
        consumer.subscribe(Collections.singleton(kafkaProperties.getTopic()));
        return consumer;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public KafkaMessageListener kafkaMessageListener(MongoOperationConsumer mongoOperationConsumer) {
        return new KafkaMessageListener(
                Executors.newSingleThreadExecutor(),
                mongoOperationConsumer
        );
    }

    @Bean
    public ConsumerRecordAdapterProcessor consumerRecordAdapterProcessor(JacksonConfiguration jacksonConfiguration,
                                                                         MongoOperationDelegatingProcessorRegistry mongoOperationDelegatingProcessorRegistry) {
        return new ConsumerRecordAdapterProcessor(
                jacksonConfiguration.objectMapper(),
                mongoOperationDelegatingProcessorRegistry
        );
    }

    @Bean
    public MongoOperationDelegatingProcessorRegistry mongoOperationDelegatingProcessorRegistry() {
        return new MongoOperationDelegatingProcessorRegistry();
    }

    @Bean
    public MongoOperationConsumer mongoOperationConsumer(Consumer<String, String> kafkaConsumer,
                                                         ConsumerRecordProcessor<String, String> consumerRecordProcessor) {
        return new MongoOperationConsumer(kafkaConsumer, consumerRecordProcessor);
    }
}
