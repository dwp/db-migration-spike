package uk.gov.dwp.personal.details.server.dao.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import uk.gov.dwp.common.kafka.mongo.api.MongoDeleteMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoInsertMessage;
import uk.gov.dwp.common.kafka.mongo.api.MongoUpdateMessage;
import uk.gov.dwp.common.kafka.mongo.producer.MongoOperationKafkaMessageDispatcher;
import uk.gov.dwp.common.mongo.DocumentWithIdConverter;
import uk.gov.dwp.personal.details.server.dao.PersonalDetailsDao;
import uk.gov.dwp.personal.details.server.model.PersonalDetails;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import static uk.gov.dwp.common.mongo.DocumentWithIdConverter._ID;

public class MongoPersonalDetailsDao implements PersonalDetailsDao {

    private final String dbName;
    private final String collectionName;
    private final MongoCollection<Document> mongoCollection;
    private final DocumentWithIdConverter<PersonalDetails, PersonalDetailsId> documentConverter;
    private final MongoOperationKafkaMessageDispatcher kafkaMessageDispatcher;

    public MongoPersonalDetailsDao(String dbName, String collectionName,
                                   MongoCollection<Document> mongoCollection,
                                   DocumentWithIdConverter<PersonalDetails, PersonalDetailsId> documentConverter,
                                   MongoOperationKafkaMessageDispatcher kafkaMessageDispatcher) {
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.mongoCollection = mongoCollection;
        this.documentConverter = documentConverter;
        this.kafkaMessageDispatcher = kafkaMessageDispatcher;
    }

    @Override
    public void create(PersonalDetails personalDetails) {
        Document document = documentConverter.convertFromObject(personalDetails);
        mongoCollection.insertOne(document);
        kafkaMessageDispatcher.send(new MongoInsertMessage(dbName, collectionName, document));
    }

    @Override
    public PersonalDetails findById(PersonalDetailsId personalDetailsId) {
        return documentConverter.convertToObject(
                mongoCollection.find(new Document(documentConverter.createId(personalDetailsId))).first()
        );
    }

    @Override
    public void update(PersonalDetails personalDetails) {
        Document document = documentConverter.convertFromObject(personalDetails);
        UpdateResult result = mongoCollection.updateOne(
                documentConverter.createId(personalDetails.getPersonalDetailsId()),
                new Document("$set", removeId(document))
        );
        if (result.getModifiedCount() > 0) {
            kafkaMessageDispatcher.send(new MongoUpdateMessage(dbName, collectionName, document));
        }
    }

    private Document removeId(Document original) {
        Document document = new Document(original);
        document.remove(_ID);
        return document;
    }

    @Override
    public void delete(PersonalDetailsId personalDetailsId) {
        Document id = documentConverter.createId(personalDetailsId);
        DeleteResult result = mongoCollection.deleteOne(id);
        if (result.getDeletedCount() > 0) {
            kafkaMessageDispatcher.send(new MongoDeleteMessage(dbName, collectionName, id));
        }
    }
}
