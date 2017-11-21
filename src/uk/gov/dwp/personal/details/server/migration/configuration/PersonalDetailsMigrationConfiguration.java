package uk.gov.dwp.personal.details.server.migration.configuration;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.migration.mongo.DocumentMigrationService;
import uk.gov.dwp.migration.mongo.MongoDocumentSelector;
import uk.gov.dwp.migration.mongo.MongoDocumentWriter;
import uk.gov.dwp.migration.mongo.api.DocumentMigrator;
import uk.gov.dwp.migration.mongo.kafka.consumer.configuration.KafkaConsumerConfiguration;
import uk.gov.dwp.personal.details.server.migration.NameSplitter;
import uk.gov.dwp.personal.details.server.migration.PersonalDetailsMigrator;

@Configuration
@Import({
        KafkaConsumerConfiguration.class
})
public class PersonalDetailsMigrationConfiguration {

    @Bean
    public MongoDocumentSelector personalDetailsDocumentSelector(MongoCollection<Document> sourceMongoCollection) {
        return new MongoDocumentSelector(
                sourceMongoCollection,
                new Document()
        );
    }

    @Bean
    public DocumentMigrator personalDetailsDocumentMigrator() {
        return new PersonalDetailsMigrator(new NameSplitter());
    }

    @Bean
    public MongoDocumentWriter personalDetailsDocumentWriter(MongoCollection<Document> destinationMongoCollection) {
        return new MongoDocumentWriter(destinationMongoCollection);
    }

    @Bean(initMethod = "doMigration")
    public DocumentMigrationService documentMigrationService(MongoDocumentSelector personalDetailsDocumentSelector,
                                                             MongoDocumentWriter personalDetailsDocumentWriter) {
        return new DocumentMigrationService(
                personalDetailsDocumentSelector,
                personalDetailsDocumentMigrator(),
                personalDetailsDocumentWriter
        );
    }
}
