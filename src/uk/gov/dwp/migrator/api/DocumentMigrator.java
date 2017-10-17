package uk.gov.dwp.migrator.api;

import org.bson.Document;

public interface DocumentMigrator {

    Document migrate(Document original);
}
