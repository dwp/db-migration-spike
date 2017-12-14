package uk.gov.dwp.personal.details.server.resource.v2;

import uk.gov.dwp.personal.details.client.v2.PersonalDetailsV2Response;
import uk.gov.dwp.personal.details.server.model.PersonalDetails;

public class PersonalDetailsV2ResponseAdapter {

    PersonalDetailsV2Response toPersonalDetails(PersonalDetails personalDetails) {
        if (personalDetails == null) {
            return null;
        }
        return PersonalDetailsV2Response.newPersonalDetailsResponse()
                .withPersonalDetailsId(personalDetails.getPersonalDetailsId())
                .withFirstName(personalDetails.getFirstName())
                .withLastName(personalDetails.getFirstName())
                .withDateOfBirth(personalDetails.getDateOfBirth())
                .build();
    }
}
