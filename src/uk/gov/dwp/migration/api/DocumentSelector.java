package uk.gov.dwp.migration.api;

public interface DocumentSelector<T> {

    Iterable<T> selectDocuments(Migration migration);

    long documentsInCollection();

    long documentsToMigrate();
}
