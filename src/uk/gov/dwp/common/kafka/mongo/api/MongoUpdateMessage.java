package uk.gov.dwp.common.kafka.mongo.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

import static uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage.MONGO_UPDATE;

@JsonTypeName(MONGO_UPDATE)
public class MongoUpdateMessage implements MongoOperation {

    public static final String MONGO_UPDATE = "MONGO_UPDATE";

    private final String db;
    private final String collection;
    private final Map<String, Object> data;

    public MongoUpdateMessage(@JsonProperty("db") String db,
                              @JsonProperty("collection") String collection,
                              @JsonProperty("dbObject") Map<String, Object> data) {
        this.db = db;
        this.collection = collection;
        this.data = data;
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
    @JsonProperty("dbObject")
    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
