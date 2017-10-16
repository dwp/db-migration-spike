package uk.gov.dwp.common.mongo;

import org.bson.Document;

public interface DocumentWithIdConverter<T, ID> extends DocumentConverter<T> {

    String _ID = "_id";

    Document createId(ID id);
}