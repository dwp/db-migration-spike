package uk.gov.dwp.personal.details.server.migration.configuration;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;
import uk.gov.dwp.migration.mongo.MongoDocumentSelector;
import uk.gov.dwp.migration.mongo.MongoDocumentWriter;
import uk.gov.dwp.migration.mongo.api.DocumentMigrator;
import uk.gov.dwp.personal.details.server.migration.NameSplitter;
import uk.gov.dwp.personal.details.server.migration.PersonalDetailsMigrator;

@Configuration
public class PersonalDetailsMigrationConfiguration {

    @Bean
    public MongoDocumentSelector personalDetailsDocumentSelector(MongoCollection<Document> sourceMongoCollection) {
        return new MongoDocumentSelector(
                sourceMongoCollection,
                new Document()
        );
    }

    @Bean
    public DocumentMigrator personalDetailsDocumentMigrator(NameSplitter nameSplitter) {
        return new PersonalDetailsMigrator(nameSplitter);
    }

    @Bean
    public NameSplitter nameSplitter() {
        return new NameSplitter();
    }

    @Bean
    public MongoDocumentWriter personalDetailsDocumentWriter(MongoCollection<Document> destinationMongoCollection,
                                                             MongoOperationKafkaMessageDispatcher mongoOperationKafaMessageDispatcher) {
        return new MongoDocumentWriter(destinationMongoCollection, mongoOperationKafaMessageDispatcher);
    }
}
