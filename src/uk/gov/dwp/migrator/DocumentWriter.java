package uk.gov.dwp.migrator;

import org.bson.Document;

public interface DocumentWriter {

    void writeDocument(Document document);
}
