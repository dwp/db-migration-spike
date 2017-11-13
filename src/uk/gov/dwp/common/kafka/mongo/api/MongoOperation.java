package uk.gov.dwp.common.kafka.mongo.api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MongoDeleteMessage.class),
        @JsonSubTypes.Type(value = MongoInsertMessage.class),
        @JsonSubTypes.Type(value = MongoUpdateMessage.class),
})
public interface MongoOperation {

    String getDb();

    String getCollection();

    Map<String, Object> getData();

}
