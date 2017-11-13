package uk.gov.dwp.migration.kafka.consumer.configuration;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.common.jackson.spring.JacksonConfiguration;
import uk.gov.dwp.common.kafka.configuration.KafkaProperties;
import uk.gov.dwp.migration.kafka.consumer.CompositeMongoOperationProcessor;
import uk.gov.dwp.migration.kafka.consumer.DbCollectionMongoOperationProcessor;
import uk.gov.dwp.migration.kafka.consumer.MongoOperationConsumer;
import uk.gov.dwp.migration.mongo.configuration.DestinationMongoDaoProperties;

import java.util.Collections;

@Configuration
@Import({
        JacksonConfiguration.class,
})
@EnableConfigurationProperties({
        KafkaProperties.class,
        DestinationMongoDaoProperties.class
})
public class KafkaConsumerConfiguration {

    @Bean
    public Consumer<String, String> kafkaConsumer(KafkaProperties kafkaProperties) {
        return new KafkaConsumer<>(kafkaProperties.toKafkaConsumerProperties());
    }

    @Bean
    public MongoOperationConsumer mongoOperationConsumer(Consumer<String, String> kafkaConsumer,
                                                         JacksonConfiguration jacksonConfiguration,
                                                         DbCollectionMongoOperationProcessor dbCollectionMongoOperationProcessor) {
        return new MongoOperationConsumer(
                kafkaConsumer,
                jacksonConfiguration.objectMapper(),
                dbCollectionMongoOperationProcessor
        );
    }

    @Bean
    public DbCollectionMongoOperationProcessor dbCollectionMongoOperationProcessor(DestinationMongoDaoProperties destinationMongoDaoProperties,
                                                                                   CompositeMongoOperationProcessor compositeMongoOperationProcessor) {
        return new DbCollectionMongoOperationProcessor(
                destinationMongoDaoProperties.getDbName(),
                destinationMongoDaoProperties.getCollection().getName(),
                compositeMongoOperationProcessor
        );
    }

    @Bean
    public CompositeMongoOperationProcessor compositeMongoOperationProcessor() {
        return new CompositeMongoOperationProcessor(Collections.emptyMap());
    }
}
