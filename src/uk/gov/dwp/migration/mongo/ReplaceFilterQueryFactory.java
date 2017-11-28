package uk.gov.dwp.migration.mongo;

import com.mongodb.QueryOperators;
import org.bson.Document;

import java.util.Arrays;

import static com.mongodb.QueryOperators.EXISTS;
import static com.mongodb.QueryOperators.LT;

public class ReplaceFilterQueryFactory {

    public Document build(Document document, Object lastModifiedDateTime) {
        Document filter = new Document("_id", document.get("_id"));
        if (lastModifiedDateTime != null) {
            filter.append(QueryOperators.OR, Arrays.asList(
                    new Document("_lastModifiedDateTime", new Document(LT, lastModifiedDateTime)),
                    new Document("_lastModifiedDateTime", new Document(EXISTS, false))
            ));
        }
        return filter;
    }
}
