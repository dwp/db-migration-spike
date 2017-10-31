package uk.gov.dwp.migration.api;

public interface InsertCommand<T> {

    void insert(T document);
}
