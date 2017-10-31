package uk.gov.dwp.migration.mongo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.Test;
import uk.gov.dwp.migration.api.Migration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MongoDocumentWriterTest {

    private final MongoCollection mongoCollection = mock(MongoCollection.class);
    private final Migration migration = mock(Migration.class);
    private final Document document = mock(Document.class);

    private final MongoDocumentWriter underTest = new MongoDocumentWriter(mongoCollection, producer);

    @Test
    public void name() throws Exception {
        underTest.writeDocument(migration, document);

        verify(migration).recordMigrated();
        verify(mongoCollection).insertOne(document);
    }

    @Test
    public void duplicateRecordIsIgnored() throws Exception {

    }
}
