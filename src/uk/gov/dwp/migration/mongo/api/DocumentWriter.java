package uk.gov.dwp.migration.mongo.api;

import org.bson.Document;

public interface DocumentWriter {

    void writeDocument(Migration migration, Document document);
}
