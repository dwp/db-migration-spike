package uk.gov.dwp.migration.mongo.api;

import org.bson.Document;

/**
 * A DocumentMigrator should transform the original Document to it's new Migrated form
 * @see LastModifiedDocumentMigrator
 */
public interface DocumentMigrator {

    Document migrate(Document original);

    /**
     *
     * @param original document to be inspected
     * @return {@code true} if the document needs to be migrated
     */
    default boolean requiresMigration(Document original) {
        return true;
    }
}
