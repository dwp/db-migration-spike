package uk.gov.dwp.common.kafka.mongo.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

import static uk.gov.dwp.common.kafka.mongo.api.MongoDeleteMessage.MONGO_DELETE;

@JsonTypeName(MONGO_DELETE)
public class MongoDeleteMessage implements MongoOperation {

    public static final String MONGO_DELETE = "MONGO_DELETE";

    private final String db;
    private final String collection;
    private final Map<String, Object> key;

    public MongoDeleteMessage(@JsonProperty("db") String db,
                              @JsonProperty("collection") String collection,
                              @JsonProperty("key") Map<String, Object> key) {
        this.db = db;
        this.collection = collection;
        this.key = key;
    }

    @Override
    public String getDb() {
        return db;
    }

    @Override
    public String getCollection() {
        return collection;
    }

    @Override
    @JsonProperty("key")
    public Map<String, Object> getData() {
        return key;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
