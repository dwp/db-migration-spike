package uk.gov.dwp.migrator;

public interface DocumentSelector<T> {

    Iterable<T> selectDocuments();

    long totalRecords();

    long recordsToMigrate();
}
