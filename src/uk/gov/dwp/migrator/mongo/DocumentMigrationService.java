package uk.gov.dwp.migrator.mongo;

import org.bson.Document;
import uk.gov.dwp.migrator.api.DocumentMigrator;
import uk.gov.dwp.migrator.api.DocumentSelector;
import uk.gov.dwp.migrator.api.DocumentWriter;
import uk.gov.dwp.migrator.api.Migration;

import java.util.function.Supplier;

public class DocumentMigrationService {

    private final DocumentSelector<Document> documentSelector;
    private final DocumentMigrator documentMigrator;
    private final DocumentWriter documentWriter;
    private final Supplier<Migration> migrationFactory;

    public DocumentMigrationService(MongoDocumentSelector documentSelector,
                                    DocumentMigrator documentMigrator,
                                    DocumentWriter documentWriter) {
        this(documentSelector, documentMigrator, documentWriter, Migration::start);
    }

    DocumentMigrationService(MongoDocumentSelector documentSelector,
                             DocumentMigrator documentMigrator,
                             DocumentWriter documentWriter,
                             Supplier<Migration> migrationFactory) {
        this.documentSelector = documentSelector;
        this.documentMigrator = documentMigrator;
        this.documentWriter = documentWriter;
        this.migrationFactory = migrationFactory;
    }

    public Migration doMigration() {
        Migration migration = migrationFactory.get();
        documentSelector
                .selectDocuments(migration)
                .forEach(document -> documentWriter.writeDocument(migration, documentMigrator.migrate(document)));
        migration.complete();
        return migration;
    }
}
