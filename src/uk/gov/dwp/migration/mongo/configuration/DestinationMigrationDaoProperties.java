package uk.gov.dwp.migration.mongo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import uk.gov.dwp.common.kafka.configuration.KafkaProperties;

@Component
@ConfigurationProperties(prefix = "migration.destination")
public class DestinationMigrationDaoProperties  {

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
