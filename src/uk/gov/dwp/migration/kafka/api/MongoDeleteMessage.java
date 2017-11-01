package uk.gov.dwp.migration.kafka.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

@JsonTypeName("MONGO_DELETE")
public class MongoDeleteMessage implements MongoOperation {

    private final String db;
    private final String collection;
    private final Map key;

    public MongoDeleteMessage(@JsonProperty("db") String db,
                              @JsonProperty("collection") String collection,
                              @JsonProperty("key") Map key) {
        this.db = db;
        this.collection = collection;
        this.key = key;
    }

    public String getDb() {
        return db;
    }

    public String getCollection() {
        return collection;
    }

    public Object getKey() {
        return key;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
