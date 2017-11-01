package uk.gov.dwp.migration.kafka.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.UUID;

public class KafkaMessageWrapper<T extends MongoOperation> {

    private final String type;
    private final TraceId traceId;
    private final UUID unitOfWorkId;
    private final Instant timestamp;
    private final String version;
    private final T message;

    public KafkaMessageWrapper(@JsonProperty("type") String type,
                               @JsonProperty("traceId") TraceId traceId,
                               @JsonProperty("unitOfWorkId") UUID unitOfWorkId,
                               @JsonProperty("timestamp") Instant timestamp,
                               @JsonProperty("version") String version,
                               @JsonProperty("message") T message) {
        this.type = type;
        this.traceId = traceId;
        this.unitOfWorkId = unitOfWorkId;
        this.timestamp = timestamp;
        this.version = version;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public UUID getUnitOfWorkId() {
        return unitOfWorkId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getVersion() {
        return version;
    }

    public T getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
