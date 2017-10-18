package uk.gov.dwp.migration.api;

import org.bson.Document;

public interface DocumentMigrator {

    Document migrate(Document original);
}
