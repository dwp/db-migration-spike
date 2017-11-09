package uk.gov.dwp.migration.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.junit.Test;
import uk.gov.dwp.common.jackson.spring.JacksonConfiguration;
import uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage;

import java.util.UUID;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class MongoUpdateMessageTest {

    private static final String DB_NAME = "dbName";
    private static final String COLLECTION_NAME = "collectionName";
    private static final String ID = UUID.randomUUID().toString();

    private final ObjectMapper objectMapper = new JacksonConfiguration().objectMapper();

    @Test
    public void serializeMongoInsertMessage() throws Exception {
        String mongoInsertMessage = objectMapper.writeValueAsString(new MongoUpdateMessage(
                DB_NAME,
                COLLECTION_NAME,
                new Document("_id", ID).append("name", "Bob Builder").append("dob", 19870910)
        ));
        assertEquals("{" +
                "  @type: \"MONGO_UPDATE\", " +
                "  db: \"" + DB_NAME + "\", " +
                "  collection: \"" + COLLECTION_NAME + "\", " +
                "  dbObject: { " +
                "    _id:\"" + ID + "\", " +
                "    name: \"Bob Builder\", " +
                "    dob: 19870910" +
                "  } " +
                "}", mongoInsertMessage, true);
    }
}
