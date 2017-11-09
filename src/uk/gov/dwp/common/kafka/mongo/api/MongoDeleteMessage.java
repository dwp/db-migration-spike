package uk.gov.dwp.common.kafka.mongo.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

@JsonTypeName("MONGO_DELETE")
public class MongoDeleteMessage implements MongoOperation {

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

    public Map<String, Object> getKey() {
        return key;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
