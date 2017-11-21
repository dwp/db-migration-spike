package uk.gov.dwp.personal.details.server.migration;

import org.bson.Document;
import uk.gov.dwp.migration.mongo.api.DocumentMigrator;

import java.time.Instant;

public class PersonalDetailsMigrator implements DocumentMigrator {

    private final NameSplitter nameSplitter;

    public PersonalDetailsMigrator(NameSplitter nameSplitter) {
        this.nameSplitter = nameSplitter;
    }

    @Override
    public Document migrate(Document original) {
        Document updated = new Document(original);
        NameSplitter.Names names = nameSplitter.split((String) updated.remove("name"));
        updated.put("firstName", names.getFirstName());
        updated.put("lastName", names.getLastName());
        updated.put("_lastModifiedDateTime", Instant.now());
        return updated;
    }

}