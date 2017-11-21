package uk.gov.dwp.migration.mongo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import uk.gov.dwp.common.kafka.configuration.KafkaProperties;

@Component
@ConfigurationProperties(prefix = "migration.source")
public class SourceMigrationDaoProperties {

    private MongoDaoProperties mongo;
    private KafkaProperties kafkaConsumer;

    public MongoDaoProperties getMongo() {
        return mongo;
    }

    public void setMongo(MongoDaoProperties mongo) {
        this.mongo = mongo;
    }

    public KafkaProperties getKafkaConsumer() {
        return kafkaConsumer;
    }

    public void setKafkaConsumer(KafkaProperties kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }
}
