package uk.gov.dwp.migration.mongo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.migration.mongo.api.DocumentSelector;
import uk.gov.dwp.migration.mongo.api.Migration;

public class MongoDocumentSelector implements DocumentSelector<Document> {

    private static Logger LOGGER = LoggerFactory.getLogger(MongoDocumentSelector.class);

    private final MongoCollection<Document> mongoCollection;
    private final String collectionName;
    private final Bson query;

    public MongoDocumentSelector(MongoCollection<Document> mongoCollection,
                                 Bson query) {
        this.mongoCollection = mongoCollection;
        this.collectionName = mongoCollection.getNamespace().getFullName();
        this.query = query;
    }

    @Override
    public Iterable<Document> selectDocuments(Migration migration) {
        migration.setTotalDocumentsToMigrate(documentsToMigrate());
        LOGGER.info("Selecting documents from collection: {}", collectionName);
        return mongoCollection.find(query);
    }

    @Override
    public long documentsInCollection() {
        LOGGER.info("Calculating number of documents in collection: {}", collectionName);
        long count = mongoCollection.count();
        LOGGER.info("{} documents in collection: '{}'", count, collectionName);
        return count;
    }

    @Override
    public long documentsToMigrate() {
        LOGGER.info("Calculating number of documents to migrate in collection: {}", collectionName);
        long count = mongoCollection.count(query);
        LOGGER.info("{} documents to migrate in collection: {} ", count, collectionName);
        return count;
    }
}
