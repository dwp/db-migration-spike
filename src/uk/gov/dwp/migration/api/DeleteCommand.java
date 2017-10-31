package uk.gov.dwp.migration.api;

public interface DeleteCommand<T> {

    void delete(T document);
}
