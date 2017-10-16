package uk.gov.dwp.migrator;

import org.bson.Document;
import uk.gov.dwp.migrator.mongo.MongoDocumentSelector;

public class DocumentMigrationService {

    private final DocumentSelector<Document> documentSelector;
    private final DocumentMigrator documentMigrator;
    private final DocumentWriter documentWriter;

    public DocumentMigrationService(MongoDocumentSelector documentSelector,
                                    DocumentMigrator documentMigrator,
                                    DocumentWriter documentWriter) {
        this.documentSelector = documentSelector;
        this.documentMigrator = documentMigrator;
        this.documentWriter = documentWriter;
    }

    public void doMigration() {
        documentSelector.selectDocuments()
                .forEach(document -> documentWriter.writeDocument(documentMigrator.migrate(document)));
    }
}
