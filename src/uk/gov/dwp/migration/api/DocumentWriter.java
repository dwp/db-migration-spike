package uk.gov.dwp.migration.api;

import org.bson.Document;

public interface DocumentWriter {

    void writeDocument(Migration migration, Document document);
}
