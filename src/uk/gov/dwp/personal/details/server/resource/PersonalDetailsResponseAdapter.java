package uk.gov.dwp.personal.details.server.resource;

import uk.gov.dwp.personal.details.client.PersonalDetailsResponse;
import uk.gov.dwp.personal.details.server.model.PersonalDetails;

public class PersonalDetailsResponseAdapter {

    PersonalDetailsResponse toPersonalDetails(PersonalDetails personalDetails) {
        return PersonalDetailsResponse.newPersonalDetailsResponse()
                .withPersonalDetailsId(personalDetails.getPersonalDetailsId())
                .withName(personalDetails.getName())
                .withDateOfBirth(personalDetails.getDateOfBirth())
                .build();
    }
}
