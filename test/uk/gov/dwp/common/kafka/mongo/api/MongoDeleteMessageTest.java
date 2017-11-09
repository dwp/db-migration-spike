package uk.gov.dwp.migration.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.junit.Test;
import uk.gov.dwp.common.jackson.spring.JacksonConfiguration;
import uk.gov.dwp.common.kafka.mongo.api.MongoDeleteMessage;

import java.util.UUID;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class MongoDeleteMessageTest {

    private static final String DB_NAME = "dbName";
    private static final String COLLECTION_NAME = "collectionName";
    private static final String ID = UUID.randomUUID().toString();

    private final ObjectMapper objectMapper = new JacksonConfiguration().objectMapper();

    @Test
    public void serializeMongoDeleteMessage() throws Exception {
        String mongoInsertMessage = objectMapper.writeValueAsString(new MongoDeleteMessage(
                DB_NAME,
                COLLECTION_NAME,
                new Document("_id", ID)
        ));
        assertEquals("{" +
                "  @type: \"MONGO_DELETE\", " +
                "  db: \"" + DB_NAME + "\", " +
                "  collection: \"" + COLLECTION_NAME + "\", " +
                "  key: { " +
                "    _id:\"" + ID + "\" " +
                "  } " +
                "}", mongoInsertMessage, true);
    }

    @Test
    public void serializeMongoDeleteMessageWithMultiDocumentId() throws Exception {
        String mongoInsertMessage = objectMapper.writeValueAsString(new MongoDeleteMessage(
                DB_NAME,
                COLLECTION_NAME,
                new Document("_id", new Document("foo", "bar"))
        ));
        assertEquals("{" +
                "  @type: \"MONGO_DELETE\", " +
                "  db: \"" + DB_NAME + "\", " +
                "  collection: \"" + COLLECTION_NAME + "\", " +
                "  key: { " +
                "    _id: { foo: \"bar\" }" +
                "  } " +
                "}", mongoInsertMessage, true);
    }
}
