package uk.gov.dwp.personal.details.server.dao.mongo.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.common.jackson.spring.JacksonConfiguration;
import uk.gov.dwp.common.kafka.mongo.producer.KafkaProducerRecordFactory;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;

@Configuration
@Import({
        JacksonConfiguration.class,
})
@EnableConfigurationProperties(PersonalDetailsDaoProperties.class)
public class KafkaProducerConfig {

    @Bean
    public Producer<String, String> kafkaProducer(PersonalDetailsDaoProperties personalDetailsDaoProperties) {
        return new KafkaProducer<>(personalDetailsDaoProperties.getKafkaProducer().toKafkaProducerProperties());
    }

    @Bean
    public MongoOperationKafkaMessageDispatcher kafkaMessageDispatcher(PersonalDetailsDaoProperties personalDetailsDaoProperties,
                                                                       ObjectMapper objectMapper,
                                                                       Producer<String, String> kafkaProducer) {
        return new MongoOperationKafkaMessageDispatcher(
                kafkaProducer,
                new KafkaProducerRecordFactory(objectMapper, personalDetailsDaoProperties.getKafkaProducer().getTopic())
        );
    }
}
