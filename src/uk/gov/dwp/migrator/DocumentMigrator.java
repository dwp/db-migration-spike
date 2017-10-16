package uk.gov.dwp.migrator;

import org.bson.Document;

public interface DocumentMigrator {

    Document migrate(Document original);
}
