package uk.gov.dwp.migration.api;

public interface UpdateCommand<T> {

    void update(T document);
}
