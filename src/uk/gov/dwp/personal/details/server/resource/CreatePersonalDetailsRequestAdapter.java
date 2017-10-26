package uk.gov.dwp.personal.details.server.resource;

import uk.gov.dwp.personal.details.client.CreatePersonalDetailsRequest;
import uk.gov.dwp.personal.details.server.model.PersonalDetails;

public class CreatePersonalDetailsRequestAdapter {

    PersonalDetails toPersonalDetails(CreatePersonalDetailsRequest createPersonalDetailsRequest) {
        return new PersonalDetails(
                createPersonalDetailsRequest.getPersonalDetailsId(),
                createPersonalDetailsRequest.getName(),
                createPersonalDetailsRequest.getDateOfBirth()
        );
    }
}
