package uk.gov.dwp.personal.details.server.dao.mongo;

import org.bson.Document;
import uk.gov.dwp.common.mongo.DocumentWithIdConverter;
import uk.gov.dwp.personal.details.server.model.PersonalDetails;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PersonalDetailsDocumentConverter implements DocumentWithIdConverter<PersonalDetails, PersonalDetailsId> {

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    static final String FIRST_NAME_FIELD = "firstName";
    static final String LAST_NAME_FIELD = "lastName";
    static final String DATE_OF_BIRTH_FIELD = "dateOfBirth";

    @Override
    public Document createId(PersonalDetailsId personalDetailsId) {
        return new Document(_ID, personalDetailsId.getId().toString());
    }

    @Override
    public PersonalDetails convertToObject(Document document) {
        if (document == null) {
            return null;
        }
        return new PersonalDetails(
                PersonalDetailsId.fromString(document.getString(_ID)),
                document.getString(FIRST_NAME_FIELD),
                document.getString(LAST_NAME_FIELD),
                LocalDate.parse(document.getString(DATE_OF_BIRTH_FIELD), LOCAL_DATE_FORMATTER)
        );
    }

    @Override
    public Document convertFromObject(PersonalDetails personalDetails) {
        return createId(personalDetails.getPersonalDetailsId())
                .append(FIRST_NAME_FIELD, personalDetails.getFirstName())
                .append(LAST_NAME_FIELD, personalDetails.getLastName())
                .append(DATE_OF_BIRTH_FIELD, personalDetails.getDateOfBirth().format(LOCAL_DATE_FORMATTER));
    }
}
