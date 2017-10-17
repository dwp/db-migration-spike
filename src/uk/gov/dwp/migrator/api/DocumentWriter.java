package uk.gov.dwp.migrator.api;

import org.bson.Document;

public interface DocumentWriter {

    void writeDocument(Migration migration, Document document);
}
