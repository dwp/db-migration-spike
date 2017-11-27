package uk.gov.dwp.common.kafka.mongo.api.test.support;

import org.bson.Document;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.core.IsAnything;
import uk.gov.dwp.common.kafka.mongo.api.MongoDeleteMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoInsertMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoOperation;
import uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage;

import static org.hamcrest.core.IsEqual.equalTo;

public class MongoOperationMatcher<T extends MongoOperation> extends TypeSafeDiagnosingMatcher<T> {

    private final Matcher<Document> documentMatcher;
    private Matcher<String> dbName = new IsAnything<>();
    private Matcher<String> collectionName = new IsAnything<>();

    private MongoOperationMatcher(Matcher<Document> dbObjectMatcher) {
        this.documentMatcher = dbObjectMatcher;
    }

    public static MongoOperationMatcher<MongoInsertMessage> mongoInsertOperation(Matcher<Document> dbObjectMatcher) {
        return new MongoOperationMatcher<>(dbObjectMatcher);
    }

    public static MongoOperationMatcher<MongoUpdateMessage> mongoUpdateOperation(Matcher<Document> dbObjectMatcher) {
        return new MongoOperationMatcher<>(dbObjectMatcher);
    }

    public static MongoOperationMatcher<MongoDeleteMessage> mongoDeleteOperation(Matcher<Document> dbObjectMatcher) {
        return new MongoOperationMatcher<>(dbObjectMatcher);
    }

    @Override
    protected boolean matchesSafely(T mongoOperation, Description description) {
        return dbName.matches(mongoOperation.getDb()) &&
                collectionName.matches(mongoOperation.getCollection()) &&
                documentMatcher.matches(mongoOperation.getData());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("db=").appendDescriptionOf(dbName)
                .appendText(", collection=").appendDescriptionOf(collectionName)
                .appendText(", dbObject=").appendDescriptionOf(documentMatcher);
    }

    public <T extends MongoOperation> MongoOperationMatcher<T> withDbName(String dbName) {
        this.dbName = equalTo(dbName);
        return (MongoOperationMatcher<T>)this;
    }

    public <T extends MongoOperation> MongoOperationMatcher<T> withCollectionName(String collectionName) {
        this.collectionName = equalTo(collectionName);
        return (MongoOperationMatcher<T>)this;
    }
}