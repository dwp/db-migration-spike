package uk.gov.dwp.personal.details.server.dao.mongo.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import uk.gov.dwp.common.kafka.configuration.KafkaProperties;

@Component
@ConfigurationProperties(prefix = "dao")
public class PersonalDetailsDaoProperties {

    private MongoDaoProperties mongo;
    private KafkaProperties kafkaProducer;

    public MongoDaoProperties getMongo() {
        return mongo;
    }

    public void setMongo(MongoDaoProperties mongo) {
        this.mongo = mongo;
    }

    public KafkaProperties getKafkaProducer() {
        return kafkaProducer;
    }

    public void setKafkaProducer(KafkaProperties kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }
}
