package uk.gov.dwp.migration.mongo.api;

import org.bson.Document;

import java.time.Instant;

public abstract class LastModifiedDocumentMigrator implements DocumentMigrator {

    @Override
    public Document migrate(Document original) {
        Document updated = new Document(original);
        migrateDocument(updated);
        updated.put("_lastModifiedDateTime", Instant.now());
        return updated;
    }

    protected abstract void migrateDocument(Document updated);
}
