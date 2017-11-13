package uk.gov.dwp.migration.kafka.producer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import uk.gov.dwp.common.jackson.spring.JacksonConfiguration;
import uk.gov.dwp.common.kafka.mongo.api.KafkaMessageWrapper;
import uk.gov.dwp.common.kafka.mongo.api.MongoDeleteMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoInsertMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;
import uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage;
import uk.gov.dwp.common.kafka.mongo.api.TraceId;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class KafkaMessageWrapperTest {

    private static final String DB_NAME = "agent-core";
    private static final String COLLECTION_NAME = "agentToDo";
    private static final String VERSION = "agent-core-1.SNAPSHOT";
    private static final Map<TraceId, Class<? extends MongoOperation>> MONGO_OPERATIONS = new HashMap<TraceId, Class<? extends MongoOperation>>() {{
        put(TraceId.fromString("f88a6c17-72c3-4006-855d-269f3bdb68f1"), MongoInsertMessage.class);
        put(TraceId.fromString("3a72a293-f64c-4568-afdf-f8aa93f4015e"), MongoInsertMessage.class);
        put(TraceId.fromString("6141c714-0d35-4946-a932-0c9d657bd3f3"), MongoUpdateMessage.class);
        put(TraceId.fromString("cfef5ebc-ac88-48d0-9f5e-e47f014ae970"), MongoUpdateMessage.class);
        put(TraceId.fromString("37ae2a7c-1c9b-453d-a00c-a1713642f1bd"), MongoUpdateMessage.class);
        put(TraceId.fromString("55c7cec5-48de-41f6-be2a-f85db49d0c84"), MongoDeleteMessage.class);
        put(TraceId.fromString("0d35cec5-4006-afdf-48d0-6141c714abc6"), MongoDeleteMessage.class);
    }};
    private final ObjectMapper objectMapper = new JacksonConfiguration().objectMapper();

    @Test
    public void canParseExampleKafkaMessages() throws Exception {
        List<KafkaMessageWrapper> messages = objectMapper.readValue(
                getClass().getResourceAsStream("/sampleKafkaMessages.json"),
                new TypeReference<List<KafkaMessageWrapper>>() {}
        );
        for (KafkaMessageWrapper underTest : messages) {
            assertThat(underTest.getType(), equalTo("V2"));
            assertThat(underTest.getTraceId(), notNullValue(TraceId.class));
            assertThat(underTest.getUnitOfWorkId(), notNullValue(UUID.class));
            assertThat(underTest.getTimestamp(), notNullValue(Instant.class));
            assertThat(underTest.getVersion(), equalTo(VERSION));
            assertThat(underTest.getMessage().getClass(), equalTo(MONGO_OPERATIONS.get(underTest.getTraceId())));
            assertThat(underTest.getMessage().getDb(), equalTo(DB_NAME));
            assertThat(underTest.getMessage().getCollection(), equalTo(COLLECTION_NAME));
        }
    }
}
