package uk.gov.dwp.migration.kafka.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

@JsonTypeName("MONGO_INSERT")
public class MongoInsertMessage implements MongoOperation {

    private final String db;
    private final String collection;
    private final Map<String, Object> dbObject;

    public MongoInsertMessage(@JsonProperty("db") String db,
                              @JsonProperty("collection") String collection,
                              @JsonProperty("dbObject") Map<String, Object> dbObject) {
        this.db = db;
        this.collection = collection;
        this.dbObject = dbObject;
    }

    @Override
    public String getDb() {
        return db;
    }

    @Override
    public String getCollection() {
        return collection;
    }

    public Map<String, Object> getDbObject() {
        return dbObject;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
