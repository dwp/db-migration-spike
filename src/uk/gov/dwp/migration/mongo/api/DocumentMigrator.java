package uk.gov.dwp.migration.mongo.api;

import org.bson.Document;

public interface DocumentMigrator {

    Document migrate(Document original);
}
