package uk.gov.dwp.migration.mongo;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;
import uk.gov.dwp.migration.mongo.api.DocumentMigrator;
import uk.gov.dwp.migration.mongo.kafka.api.MongoOperationProcessor;

public class MongoUpdateOperationProcessor implements MongoOperationProcessor<MongoUpdateMessage> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MongoUpdateOperationProcessor.class);

    private final MongoCollection<Document> mongoCollection;
    private final String db;
    private final String collection;
    private final MongoOperationKafkaMessageDispatcher mongoOperationKafkaMessageDispatcher;
    private final DocumentMigrator documentMigrator;
    private final ReplaceFilterQueryFactory replaceFilterQueryFactory;

    public MongoUpdateOperationProcessor(MongoCollection<Document> mongoCollection,
                                         String db,
                                         String collection,
                                         MongoOperationKafkaMessageDispatcher mongoOperationKafkaMessageDispatcher,
                                         DocumentMigrator documentMigrator,
                                         ReplaceFilterQueryFactory replaceFilterQueryFactory) {
        this.mongoCollection = mongoCollection;
        this.db = db;
        this.collection = collection;
        this.mongoOperationKafkaMessageDispatcher = mongoOperationKafkaMessageDispatcher;
        this.documentMigrator = documentMigrator;
        this.replaceFilterQueryFactory = replaceFilterQueryFactory;
    }

    @Override
    public void process(MongoUpdateMessage mongoUpdateMessage) {
        Document originalDocument = new Document(mongoUpdateMessage.getData());
        Document migratedDocument = documentMigrator.migrate(originalDocument);
        try {
            UpdateResult updateResult = mongoCollection.replaceOne(
                    replaceFilterQueryFactory.build(migratedDocument, originalDocument.get("_lastModifiedDateTime")),
                    migratedDocument
            );
            if (updateResult.getModifiedCount() == 0) {
                mongoCollection.insertOne(migratedDocument);
            }
            mongoOperationKafkaMessageDispatcher.send(new MongoUpdateMessage(db, collection, migratedDocument));
        } catch (MongoWriteException e) {
            // TODO: Should we try and findOneAndReplace??
            LOGGER.warn("Unable to update: {}", migratedDocument.get("_id"));
        }
    }
}
