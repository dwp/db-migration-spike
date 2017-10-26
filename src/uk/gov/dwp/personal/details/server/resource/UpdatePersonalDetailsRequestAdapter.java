package uk.gov.dwp.personal.details.server.resource;

import uk.gov.dwp.personal.details.client.UpdatePersonalDetailsRequest;
import uk.gov.dwp.personal.details.server.model.PersonalDetails;

public class UpdatePersonalDetailsRequestAdapter {

    PersonalDetails toPersonalDetails(UpdatePersonalDetailsRequest updatePersonalDetailsRequest) {
        return new PersonalDetails(
                updatePersonalDetailsRequest.getPersonalDetailsId(),
                updatePersonalDetailsRequest.getName(),
                updatePersonalDetailsRequest.getDateOfBirth()
        );
    }
}
