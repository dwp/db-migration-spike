package uk.gov.dwp.personal.details.server.migration;

import org.bson.Document;
import uk.gov.dwp.migration.mongo.api.LastModifiedDocumentMigrator;

public class PersonalDetailsMigrator extends LastModifiedDocumentMigrator {

    private final NameSplitter nameSplitter;

    public PersonalDetailsMigrator(NameSplitter nameSplitter) {
        this.nameSplitter = nameSplitter;
    }

    @Override
    protected void migrateDocument(Document updated) {
        NameSplitter.Names names = nameSplitter.split((String) updated.remove("name"));
        updated.put("firstName", names.getFirstName());
        updated.put("lastName", names.getLastName());
    }

}