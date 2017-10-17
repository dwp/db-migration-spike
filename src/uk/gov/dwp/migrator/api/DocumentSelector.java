package uk.gov.dwp.migrator.api;

public interface DocumentSelector<T> {

    Iterable<T> selectDocuments(Migration migration);

    long documentsInCollection();

    long documentsToMigrate();
}
