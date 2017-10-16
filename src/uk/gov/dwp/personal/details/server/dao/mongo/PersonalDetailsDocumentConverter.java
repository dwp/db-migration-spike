package uk.gov.dwp.personal.details.server.dao.mongo;

import org.bson.Document;
import uk.gov.dwp.api.PersonalDetails;
import uk.gov.dwp.api.PersonalDetailsId;
import uk.gov.dwp.common.mongo.DocumentWithIdConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PersonalDetailsDocumentConverter implements DocumentWithIdConverter<PersonalDetails, PersonalDetailsId> {

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String NAME_FIELD = "name";
    private static final String DATE_OF_BIRTH_FIELD = "dateOfBirth";

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
                document.getString(NAME_FIELD),
                LocalDate.parse(document.getString(DATE_OF_BIRTH_FIELD), LOCAL_DATE_FORMATTER)
        );
    }

    @Override
    public Document convertFromObject(PersonalDetails personalDetails) {
        return createId(personalDetails.getPersonalDetailsId())
                .append(NAME_FIELD, personalDetails.getName())
                .append(DATE_OF_BIRTH_FIELD, personalDetails.getDateOfBirth().format(LOCAL_DATE_FORMATTER));
    }
}
