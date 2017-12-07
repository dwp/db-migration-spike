package uk.gov.dwp.migration.mongo.api;

import org.bson.Document;

public interface DocumentWriter {

    /**
     * Write the migrated document back to Mongo
     * @param migration
     * @param document the migrated document
     */
    void writeDocument(Migration migration, Document document);
}
