package uk.gov.dwp.migration.kafka.consumer.configuration;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.common.jackson.spring.JacksonConfiguration;
import uk.gov.dwp.migration.kafka.consumer.CompositeMongoOperationProcessor;
import uk.gov.dwp.migration.kafka.consumer.MongoOperationConsumer;

import java.util.Collections;
import java.util.Properties;

@Configuration
@Import({
        JacksonConfiguration.class,
})
public class KafkaConsumerConfiguration {

    @Bean
    public Consumer<String, String> kafkaConsumer() {
        return new KafkaConsumer<>(new Properties());
    }

    @Bean
    public MongoOperationConsumer mongoOperationConsumer(Consumer<String, String> kafkaConsumer,
                                                         JacksonConfiguration jacksonConfiguration) {
        return new MongoOperationConsumer(
                kafkaConsumer,
                jacksonConfiguration.objectMapper(),
                compositeMongoOperationProcessor()
        );
    }

    @Bean
    public CompositeMongoOperationProcessor compositeMongoOperationProcessor() {
        return new CompositeMongoOperationProcessor(Collections.emptyMap());
    }
}
