package uk.gov.dwp.migration.mongo;

import com.mongodb.MongoNamespace;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.migration.api.Migration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MongoDocumentSelectorTest {

    private final MongoCollection mongoCollection = mock(MongoCollection.class);
    private final Bson query = mock(Bson.class);
    private final Migration migration = mock(Migration.class);

    private MongoDocumentSelector underTest;

    @Before
    public void setUp() {
        when(mongoCollection.getNamespace()).thenReturn(new MongoNamespace("db.collection"));

        underTest = new MongoDocumentSelector(mongoCollection, query);
    }

    @Test
    public void selectDocumentsForMigration() throws Exception {
        FindIterable results = mock(FindIterable.class);
        when(mongoCollection.count(query)).thenReturn(10L);
        when(mongoCollection.find(query)).thenReturn(results);

        assertThat(underTest.selectDocuments(migration), is(results));

        verify(migration).setTotalDocumentsToMigrate(10L);
    }

    @Test
    public void getDocumentsInCollection() throws Exception {
        when(mongoCollection.count()).thenReturn(10L);

        assertThat(underTest.documentsInCollection(), is(10L));
    }

    @Test
    public void getNumberOfDocumentsToMigrate() throws Exception {
        when(mongoCollection.count(query)).thenReturn(10L);

        assertThat(underTest.documentsToMigrate(), is(10L));
    }
}