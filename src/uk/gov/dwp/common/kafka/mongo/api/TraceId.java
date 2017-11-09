package uk.gov.dwp.common.kafka.mongo.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import uk.gov.dwp.common.id.Id;

import java.util.UUID;

public class TraceId implements Id<UUID> {

    private final UUID id;

    private TraceId(UUID id) {
        this.id = id;
    }

    public static TraceId newTraceId() {
        return new TraceId(UUID.randomUUID());
    }

    public static TraceId fromUUID(UUID uuid) {
        return new TraceId(uuid);
    }

    @JsonCreator
    public static TraceId fromString(String uuid) {
        return new TraceId(UUID.fromString(uuid));
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return id.equals(((TraceId) o).id);
    }

    @Override
    public String toString() {
        return id.toString();
    }

}
