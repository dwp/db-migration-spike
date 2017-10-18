package uk.gov.dwp.migration.mongo;

import org.bson.Document;
import org.hamcrest.Matchers;
import org.junit.Test;
import uk.gov.dwp.migration.api.DocumentMigrator;
import uk.gov.dwp.migration.api.DocumentWriter;
import uk.gov.dwp.migration.api.Migration;

import java.util.Collections;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class DocumentMigrationServiceTest {

    private final MongoDocumentSelector documentSelector = mock(MongoDocumentSelector.class);
    private final DocumentMigrator documentMigrator = mock(DocumentMigrator.class);
    private final DocumentWriter documentWriter = mock(DocumentWriter.class);
    private final Migration migration = mock(Migration.class);
    private final Document document = mock(Document.class);
    private final Document migratedDocument = mock(Document.class);

    private final DocumentMigrationService underTest = new DocumentMigrationService(
            documentSelector,
            documentMigrator,
            documentWriter,
            () -> migration
    );

    @Test
    public void noDocumentsToMigrate() {
        when(documentSelector.selectDocuments(migration)).thenReturn(emptyList());

        assertThat(underTest.doMigration(), Matchers.is(migration));

        verify(migration).complete();
        verifyNoMoreInteractions(migration);
        verifyZeroInteractions(documentMigrator);
        verifyZeroInteractions(documentWriter);
    }

    @Test
    public void documentsToMigrate() {
        when(documentSelector.selectDocuments(migration)).thenReturn(Collections.singletonList(document));
        when(documentMigrator.migrate(document)).thenReturn(migratedDocument);

        assertThat(underTest.doMigration(), Matchers.is(migration));

        verify(documentMigrator).migrate(document);
        verify(documentWriter).writeDocument(migration, migratedDocument);
        verify(migration).complete();
        verifyNoMoreInteractions(migration);
        verifyNoMoreInteractions(documentMigrator);
        verifyNoMoreInteractions(documentWriter);
    }
}