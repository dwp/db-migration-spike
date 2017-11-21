package uk.gov.dwp.personal.details.server.dao.mongo.spring;

import com.mongodb.MongoClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.common.jackson.spring.JacksonConfiguration;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;
import uk.gov.dwp.common.mongo.MongoTransformerConfiguration;
import uk.gov.dwp.personal.details.server.dao.PersonalDetailsDao;
import uk.gov.dwp.personal.details.server.dao.mongo.MongoPersonalDetailsDao;
import uk.gov.dwp.personal.details.server.dao.mongo.PersonalDetailsDocumentConverter;

@Configuration
@Import({
        JacksonConfiguration.class
})
@EnableConfigurationProperties(PersonalDetailsDaoProperties.class)
public class MongoDaoConfig {

    static {
        MongoTransformerConfiguration.registerTransformers();
    }

    @Bean
    public MongoClient mongoClient(PersonalDetailsDaoProperties personalDetailsDaoProperties) {
        return new MongoClient(
                personalDetailsDaoProperties.getMongo().createSeeds(),
                personalDetailsDaoProperties.getMongo().createCredentials(),
                personalDetailsDaoProperties.getMongo().mongoClientOptions()
        );
    }

    @Bean
    public PersonalDetailsDao personalDetailsDao(MongoClient mongoClient,
                                                 PersonalDetailsDaoProperties personalDetailsDaoProperties,
                                                 PersonalDetailsDocumentConverter personalDetailsDocumentConverter,
                                                 MongoOperationKafkaMessageDispatcher kafkaMessageDispatcher) {
        return new MongoPersonalDetailsDao(
                personalDetailsDaoProperties.getMongo().getDbName(),
                personalDetailsDaoProperties.getMongo().getPersonalDetails().getName(),
                mongoClient
                        .getDatabase(personalDetailsDaoProperties.getMongo().getDbName())
                        .getCollection(personalDetailsDaoProperties.getMongo().getPersonalDetails().getName()),
                personalDetailsDocumentConverter,
                kafkaMessageDispatcher);
    }

    @Bean
    public PersonalDetailsDocumentConverter personalDetailsDocumentConverter() {
        return new PersonalDetailsDocumentConverter();
    }
}
