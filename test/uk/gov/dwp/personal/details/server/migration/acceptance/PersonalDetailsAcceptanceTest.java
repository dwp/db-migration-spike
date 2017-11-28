package uk.gov.dwp.personal.details.server.migration.acceptance;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.dwp.common.kafka.mongo.api.MongoDeleteMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoInsertMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;
import uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage;
import uk.gov.dwp.common.kafka.mongo.api.test.support.MongoOperationMatcher;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;
import uk.gov.dwp.migration.mongo.DocumentMigrationService;
import uk.gov.dwp.migration.mongo.MongoDocumentSelector;
import uk.gov.dwp.migration.mongo.MongoDocumentWriter;
import uk.gov.dwp.migration.mongo.api.DocumentMigrator;
import uk.gov.dwp.migration.mongo.configuration.SourceMongoDaoConfig;
import uk.gov.dwp.migration.mongo.kafka.consumer.MongoOperationDelegatingProcessor;
import uk.gov.dwp.personal.details.server.migration.acceptance.PersonalDetailsAcceptanceTest.MockKafkaProducerConfig;
import uk.gov.dwp.personal.details.server.migration.configuration.PersonalDetailsMigrationConfiguration;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static uk.gov.dwp.common.kafka.mongo.api.test.support.MongoOperationMatcher.mongoUpdateOperation;
import static uk.gov.dwp.common.mongo.test.support.DocumentMatcher.hasField;
import static uk.gov.dwp.personal.details.type.PersonalDetailsId.newPersonalDetailsId;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        MockKafkaProducerConfig.class,
        PersonalDetailsMigrationConfiguration.class,
        SourceMongoDaoConfig.class,
})
@TestPropertySource("/acceptance-test.properties")
public class PersonalDetailsAcceptanceTest {

    private static final Instant CREATED_DATE_TIME = Instant.now();

    private final PersonalDetailsId personalDetailsId = newPersonalDetailsId();
    @Autowired
    private MongoCollection<Document> sourceMongoCollection;
    @Autowired
    private MongoCollection<Document> destinationMongoCollection;
    @Autowired
    private MongoDocumentSelector personalDetailsDocumentSelector;
    @Autowired
    private DocumentMigrator personalDetailsDocumentMigrator;
    @Autowired
    private MongoDocumentWriter personalDetailsDocumentWriter;
    @Autowired
    private MongoOperationDelegatingProcessor mongoOperationDelegatingProcessor;
    @Autowired
    private MongoOperationKafkaMessageDispatcher mongoOperationKafkaMessageDispatcher;

    private DocumentMigrationService documentMigrationService;
    private Clock clock = Clock.systemUTC();

    @Before
    public void setUp() {
        documentMigrationService = new DocumentMigrationService(
                personalDetailsDocumentSelector,
                personalDetailsDocumentMigrator,
                personalDetailsDocumentWriter
        );
        sourceMongoCollection.deleteOne(createId(personalDetailsId));
        destinationMongoCollection.deleteOne(createId(personalDetailsId));
        reset(mongoOperationKafkaMessageDispatcher);
    }

    @Test
    public void documentIsMigratedThenUpdateReceived() {
        Instant lastModifiedDateTime = givenARecordExistsInTheSourceCollection();

        documentMigrationService.doMigration();
        assertThat(destinationMongoCollection.find(createId(personalDetailsId)).first(), allOf(
                hasField("firstName", equalTo("Mickey")),
                hasField("lastName", equalTo("Mouse")),
                hasField("dateOfBirth", equalTo("19281118")),
                hasField("_createdDateTime", equalTo(CREATED_DATE_TIME)),
                hasField("_lastModifiedDateTime", not(equalTo(lastModifiedDateTime))),
                not(hasField("name"))
        ));
        verify(mongoOperationKafkaMessageDispatcher).send(matches(mongoUpdateOperation(allOf(
                hasField("_id", equalTo(personalDetailsId.toString())),
                hasField("firstName", equalTo("Mickey")),
                hasField("lastName", equalTo("Mouse")),
                hasField("dateOfBirth", equalTo("19281118")),
                hasField("_createdDateTime", equalTo(CREATED_DATE_TIME)),
                hasField("_lastModifiedDateTime", notNullValue())
        ))));

        mongoOperationDelegatingProcessor.process(mongoUpdate(personalDetailsV1(personalDetailsId, "Mick Mouse", "19281118")));
        assertThat(destinationMongoCollection.find(createId(personalDetailsId)).first(), allOf(
                hasField("firstName", equalTo("Mick")),
                hasField("lastName", equalTo("Mouse")),
                hasField("dateOfBirth", equalTo("19281118")),
                hasField("_createdDateTime", equalTo(CREATED_DATE_TIME)),
                hasField("_lastModifiedDateTime", not(equalTo(lastModifiedDateTime))),
                not(hasField("name"))
        ));
        verify(mongoOperationKafkaMessageDispatcher).send(matches(mongoUpdateOperation(allOf(
                hasField("_id", equalTo(personalDetailsId.toString())),
                hasField("firstName", equalTo("Mick")),
                hasField("lastName", equalTo("Mouse")),
                hasField("dateOfBirth", equalTo("19281118")),
                hasField("_createdDateTime", equalTo(CREATED_DATE_TIME)),
                hasField("_lastModifiedDateTime", notNullValue())
        ))));
    }

    private Instant givenARecordExistsInTheSourceCollection() {
        clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);
        Instant lastModifiedDateTime = clock.instant();
        sourceMongoCollection.insertOne(personalDetailsV1(personalDetailsId, "Mickey Mouse", "19281118"));
        assertThat(sourceMongoCollection.find(createId(personalDetailsId)).first(), allOf(
                hasField("name", equalTo("Mickey Mouse")),
                hasField("dateOfBirth", equalTo("19281118")),
                hasField("_createdDateTime", equalTo(CREATED_DATE_TIME)),
                hasField("_lastModifiedDateTime", equalTo(lastModifiedDateTime)),
                not(hasField("firstName")),
                not(hasField("lastName"))
        ));
        assertThat(destinationMongoCollection.find(createId(personalDetailsId)).first(), nullValue());
        clock = Clock.systemUTC();
        return lastModifiedDateTime;
    }

    @Test
    public void documentIsMigratedThenDeleteReceived() {
        Instant lastModifiedDateTime = givenARecordExistsInTheSourceCollection();

        documentMigrationService.doMigration();

        assertThat(destinationMongoCollection.find(createId(personalDetailsId)).first(), allOf(
                hasField("firstName", equalTo("Mickey")),
                hasField("lastName", equalTo("Mouse")),
                hasField("dateOfBirth", equalTo("19281118")),
                hasField("_createdDateTime", equalTo(CREATED_DATE_TIME)),
                hasField("_lastModifiedDateTime", not(equalTo(lastModifiedDateTime))),
                not(hasField("name"))
        ));
        verify(mongoOperationKafkaMessageDispatcher).send(matches(mongoUpdateOperation(allOf(
                hasField("_id", equalTo(personalDetailsId.toString())),
                hasField("firstName", equalTo("Mickey")),
                hasField("lastName", equalTo("Mouse")),
                hasField("dateOfBirth", equalTo("19281118")),
                hasField("_createdDateTime", equalTo(CREATED_DATE_TIME)),
                hasField("_lastModifiedDateTime", notNullValue())
        ))));

        mongoOperationDelegatingProcessor.process(mongoDelete(personalDetailsId));

        assertThat(destinationMongoCollection.find(createId(personalDetailsId)).first(), allOf(
                hasField("_lastModifiedDateTime", not(equalTo(lastModifiedDateTime))),
                not(hasField("firstName")),
                not(hasField("lastName")),
                not(hasField("dateOfBirth"))
        ));
    }

    @Test
    public void insertIsReceivedThenRecordIsMigrated() {
        sourceMongoCollection.insertOne(personalDetailsV1(personalDetailsId, "Mick Mouse", "19281118"));

        mongoOperationDelegatingProcessor.process(mongoInsert(personalDetailsV1(personalDetailsId, "Mick Mouse", "19281118")));
        documentMigrationService.doMigration();

        assertThat(destinationMongoCollection.find(createId(personalDetailsId)).first(), allOf(
                hasField("firstName", equalTo("Mick")),
                hasField("lastName", equalTo("Mouse")),
                hasField("dateOfBirth", equalTo("19281118"))
        ));
    }

    @Test
    public void updateIsReceivedThenRecordIsMigrated() {
        sourceMongoCollection.insertOne(personalDetailsV1(personalDetailsId, "Mick Mouse", "19281118"));

        mongoOperationDelegatingProcessor.process(mongoUpdate(personalDetailsV1(personalDetailsId, "Mickey Mouse", "19281118")));
        documentMigrationService.doMigration();

        assertThat(destinationMongoCollection.find(createId(personalDetailsId)).first(), allOf(
                hasField("firstName", equalTo("Mickey")),
                hasField("lastName", equalTo("Mouse")),
                hasField("dateOfBirth", equalTo("19281118"))
        ));
    }

    @Test
    public void deleteIsReceivedThenRecordIsMigrated() {
        sourceMongoCollection.insertOne(personalDetailsV1(personalDetailsId, "Mickey Mouse", "19281118"));

        mongoOperationDelegatingProcessor.process(mongoDelete(personalDetailsId));
        documentMigrationService.doMigration();

        assertThat(destinationMongoCollection.find(createId(personalDetailsId)).first(), allOf(
                not(hasField("firstName")),
                not(hasField("lastName")),
                not(hasField("dateOfBirth"))
        ));
    }

    private MongoInsertMessage mongoInsert(Document personalDetailsDocument) {
        return new MongoInsertMessage(
                sourceMongoCollection.getNamespace().getDatabaseName(),
                sourceMongoCollection.getNamespace().getCollectionName(),
                personalDetailsDocument
        );
    }

    private MongoUpdateMessage mongoUpdate(Document personalDetailsDocument) {
        return new MongoUpdateMessage(
                sourceMongoCollection.getNamespace().getDatabaseName(),
                sourceMongoCollection.getNamespace().getCollectionName(),
                personalDetailsDocument
        );
    }

    private MongoDeleteMessage mongoDelete(PersonalDetailsId personalDetailsId) {
        return new MongoDeleteMessage(
                sourceMongoCollection.getNamespace().getDatabaseName(),
                sourceMongoCollection.getNamespace().getCollectionName(),
                createId(personalDetailsId)
        );
    }

    private Document personalDetailsV1(PersonalDetailsId personalDetailsId, String name, String dateOfBirth) {
        return createId(personalDetailsId)
                .append("name", name)
                .append("dateOfBirth", dateOfBirth)
                .append("_createdDateTime", CREATED_DATE_TIME)
                .append("_lastModifiedDateTime", Instant.now(clock));
    }

    private Document createId(PersonalDetailsId personalDetailsId) {
        return new Document("_id", personalDetailsId.toString());
    }

    private MongoOperation matches(MongoOperationMatcher<? extends MongoOperation> mongoOperationMatcher) {
        return Mockito.argThat(new HamcrestArgumentMatcher<>(mongoOperationMatcher
                .withDbName(destinationMongoCollection.getNamespace().getDatabaseName())
                .withCollectionName(destinationMongoCollection.getNamespace().getCollectionName())
        ));
    }


    @Configuration
    public static class MockKafkaProducerConfig {

        @Bean
        public MongoOperationKafkaMessageDispatcher mongoOperationKafkaMessageDispatcher() {
            return mock(MongoOperationKafkaMessageDispatcher.class);
        }

    }

}