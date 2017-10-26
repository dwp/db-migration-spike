package uk.gov.dwp.personal.details.server.resource;

import uk.gov.dwp.personal.details.client.PersonalDetails;
import uk.gov.dwp.personal.details.client.UpdatePersonalDetailsRequest;

public class UpdatePersonalDetailsRequestAdapter {

    PersonalDetails toPersonalDetails(UpdatePersonalDetailsRequest updatePersonalDetailsRequest) {
        return new PersonalDetails(
                updatePersonalDetailsRequest.getPersonalDetailsId(),
                updatePersonalDetailsRequest.getName(),
                updatePersonalDetailsRequest.getDateOfBirth()
        );
    }
}
