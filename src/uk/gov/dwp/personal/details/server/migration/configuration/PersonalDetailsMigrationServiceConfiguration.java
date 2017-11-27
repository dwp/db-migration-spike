package uk.gov.dwp.personal.details.server.migration.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.migration.mongo.DocumentMigrationService;
import uk.gov.dwp.migration.mongo.MongoDocumentSelector;
import uk.gov.dwp.migration.mongo.MongoDocumentWriter;
import uk.gov.dwp.migration.mongo.api.DocumentMigrator;

@Configuration
@Import({
        PersonalDetailsMigrationConfiguration.class
})
public class PersonalDetailsMigrationServiceConfiguration {

    @Bean(initMethod = "doMigration")
    public DocumentMigrationService documentMigrationService(MongoDocumentSelector personalDetailsDocumentSelector,
                                                             DocumentMigrator personalDetailsDocumentMigrator,
                                                             MongoDocumentWriter personalDetailsDocumentWriter) {
        return new DocumentMigrationService(
                personalDetailsDocumentSelector,
                personalDetailsDocumentMigrator,
                personalDetailsDocumentWriter
        );
    }
}
