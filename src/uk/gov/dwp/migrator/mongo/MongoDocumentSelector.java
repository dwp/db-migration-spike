package uk.gov.dwp.migrator.mongo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.migrator.DocumentSelector;

public class MongoDocumentSelector implements DocumentSelector<Document> {

    private static Logger LOGGER = LoggerFactory.getLogger(MongoDocumentSelector.class);

    private final MongoCollection<Document> mongoCollection;
    private final Bson query;

    public MongoDocumentSelector(MongoCollection<Document> mongoCollection,
                                 Bson query) {
        this.mongoCollection = mongoCollection;
        this.query = query;
    }

    @Override
    public Iterable<Document> selectDocuments() {
        return mongoCollection.find();
    }

    @Override
    public long totalRecords() {
        long count = mongoCollection.count();
        LOGGER.info("{} documents in collection: '{}'", count, mongoCollection.getNamespace().getFullName());
        return count;
    }

    @Override
    public long recordsToMigrate() {
        long count = mongoCollection.count(query);
        LOGGER.info("{} documents to migrate in collection: {} ", count, mongoCollection.getNamespace().getFullName());
        return count;
    }
}
