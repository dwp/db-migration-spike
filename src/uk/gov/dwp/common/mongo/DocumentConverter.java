package uk.gov.dwp.common.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import org.bson.Document;

import java.util.Collection;

public interface DocumentConverter<T> {

    static BasicDBList toBasicDBList(Collection<?> collection) {
        BasicDBList basicDBList = new BasicDBList();
        basicDBList.addAll(collection);
        return basicDBList;
    }

    T convertToObject(Document dbObject);

    Document convertFromObject(T object);


}