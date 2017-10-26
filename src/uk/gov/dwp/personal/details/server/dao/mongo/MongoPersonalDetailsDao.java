package uk.gov.dwp.personal.details.server.dao.mongo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import uk.gov.dwp.common.mongo.DocumentWithIdConverter;
import uk.gov.dwp.personal.details.client.PersonalDetails;
import uk.gov.dwp.personal.details.server.dao.PersonalDetailsDao;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import static uk.gov.dwp.common.mongo.DocumentWithIdConverter._ID;

public class MongoPersonalDetailsDao implements PersonalDetailsDao {

    private final MongoCollection<Document> collection;
    private final DocumentWithIdConverter<PersonalDetails, PersonalDetailsId> documentConverter;

    public MongoPersonalDetailsDao(MongoCollection<Document> collection,
                                   DocumentWithIdConverter<PersonalDetails, PersonalDetailsId> documentConverter) {
        this.collection = collection;
        this.documentConverter = documentConverter;
    }

    @Override
    public void create(PersonalDetails personalDetails) {
        collection.insertOne(documentConverter.convertFromObject(personalDetails));
    }

    @Override
    public PersonalDetails findById(PersonalDetailsId personalDetailsId) {
        return documentConverter.convertToObject(
                collection.find(new Document(documentConverter.createId(personalDetailsId))).first()
        );
    }

    @Override
    public void update(PersonalDetails personalDetails) {
        Document document = documentConverter.convertFromObject(personalDetails);
        document.remove(_ID);
        collection.findOneAndUpdate(
                documentConverter.createId(personalDetails.getPersonalDetailsId()),
                new Document("$set", document)
        );
    }

    @Override
    public void delete(PersonalDetailsId personalDetailsId) {
        collection.findOneAndDelete(documentConverter.createId(personalDetailsId));
    }
}
