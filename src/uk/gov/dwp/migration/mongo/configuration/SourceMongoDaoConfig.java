package uk.gov.dwp.migration.mongo.configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;
import uk.gov.dwp.common.mongo.MongoTransformerConfiguration;
import uk.gov.dwp.migration.mongo.MongoDeleteOperationProcessor;
import uk.gov.dwp.migration.mongo.MongoInsertOperationProcessor;
import uk.gov.dwp.migration.mongo.MongoUpdateOperationProcessor;
import uk.gov.dwp.migration.mongo.ReplaceFilterQueryFactory;
import uk.gov.dwp.migration.mongo.api.DocumentMigrator;
import uk.gov.dwp.migration.mongo.kafka.consumer.MongoOperationDelegatingProcessor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.MongoCredential.createScramSha1Credential;
import static java.util.Collections.singletonList;

@Configuration
@EnableConfigurationProperties({
        DestinationMigrationDaoProperties.class,
        SourceMigrationDaoProperties.class
})
public class SourceMongoDaoConfig {

    static {
        MongoTransformerConfiguration.registerTransformers();
    }

    @Bean
    public MongoClient sourceMongoClient(SourceMigrationDaoProperties sourceMigrationDaoProperties) {
        return new MongoClient(
                createSeeds(sourceMigrationDaoProperties.getMongo()),
                createCredentials(sourceMigrationDaoProperties.getMongo()),
                sourceMigrationDaoProperties.getMongo().mongoClientOptions()
        );
    }

    @Bean
    public MongoCollection<Document> sourceMongoCollection(SourceMigrationDaoProperties sourceMigrationDaoProperties) {
        return sourceMongoClient(sourceMigrationDaoProperties)
                .getDatabase(sourceMigrationDaoProperties.getMongo().getDbName())
                .getCollection(sourceMigrationDaoProperties.getMongo().getCollection().getName());
    }

    @Bean
    public MongoClient destinationMongoClient(DestinationMigrationDaoProperties destinationMigrationDaoProperties) {
        return new MongoClient(
                createSeeds(destinationMigrationDaoProperties.getMongo()),
                createCredentials(destinationMigrationDaoProperties.getMongo()),
                destinationMigrationDaoProperties.getMongo().mongoClientOptions()
        );
    }

    @Bean
    public MongoCollection<Document> destinationMongoCollection(DestinationMigrationDaoProperties destinationMigrationDaoProperties) {
        return destinationMongoClient(destinationMigrationDaoProperties)
                .getDatabase(destinationMigrationDaoProperties.getMongo().getDbName())
                .getCollection(destinationMigrationDaoProperties.getMongo().getCollection().getName());
    }

    @Bean
    public MongoOperationDelegatingProcessor mongoOperationDelegatingProcessor(SourceMigrationDaoProperties sourceMigrationDaoProperties,
                                                                               MongoCollection<Document> destinationMongoCollection,
                                                                               MongoOperationKafkaMessageDispatcher mongoOperationKafkaMessageDispatcher,
                                                                               DocumentMigrator documentMigrator) {
        return new MongoOperationDelegatingProcessor(
                sourceMigrationDaoProperties.getMongo().getDbName(),
                sourceMigrationDaoProperties.getMongo().getCollection().getName(),
                new MongoInsertOperationProcessor(
                        destinationMongoCollection,
                        destinationMongoCollection.getNamespace().getDatabaseName(),
                        destinationMongoCollection.getNamespace().getCollectionName(),
                        mongoOperationKafkaMessageDispatcher,
                        documentMigrator
                ),
                new MongoUpdateOperationProcessor(
                        destinationMongoCollection,
                        destinationMongoCollection.getNamespace().getDatabaseName(),
                        destinationMongoCollection.getNamespace().getCollectionName(),
                        mongoOperationKafkaMessageDispatcher,
                        documentMigrator,
                        new ReplaceFilterQueryFactory()),
                new MongoDeleteOperationProcessor(destinationMongoCollection)
        );
    }

    private List<MongoCredential> createCredentials(MongoDaoProperties mongoDaoProperties) {
        return mongoDaoProperties.getCollection().getUsername()
                .map(username -> singletonList(createScramSha1Credential(
                        username,
                        mongoDaoProperties.getDbName(),
                        mongoDaoProperties.getCollection()
                                .getPassword()
                                .orElseThrow(() -> new IllegalArgumentException("Password is required when username specified"))
                                .toCharArray())))
                .orElse(Collections.emptyList());
    }

    private List<ServerAddress> createSeeds(MongoDaoProperties mongoDaoProperties) {
        return mongoDaoProperties.getServerAddresses()
                .stream()
                .map(serverAddress -> new ServerAddress(serverAddress.getHost(), serverAddress.getPort()))
                .collect(Collectors.toList());
    }
}
