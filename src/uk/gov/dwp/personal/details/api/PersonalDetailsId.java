package uk.gov.dwp.personal.details.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import uk.gov.dwp.common.id.Id;

import java.util.UUID;

public class PersonalDetailsId implements Id<UUID> {

    private final UUID id;

    private PersonalDetailsId(UUID id) {
        this.id = id;
    }

    public static PersonalDetailsId newPersonalDetailsId() {
        return new PersonalDetailsId(UUID.randomUUID());
    }

    public static PersonalDetailsId fromUUID(UUID uuid) {
        return new PersonalDetailsId(uuid);
    }

    @JsonCreator
    public static PersonalDetailsId fromString(String uuid) {
        return new PersonalDetailsId(UUID.fromString(uuid));
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

        return id.equals(((PersonalDetailsId) o).id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
