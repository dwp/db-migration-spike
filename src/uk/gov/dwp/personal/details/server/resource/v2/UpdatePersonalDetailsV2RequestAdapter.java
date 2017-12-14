package uk.gov.dwp.personal.details.server.resource.v2;

import uk.gov.dwp.personal.details.client.v2.UpdatePersonalDetailsV2Request;
import uk.gov.dwp.personal.details.server.model.PersonalDetails;

public class UpdatePersonalDetailsV2RequestAdapter {

    PersonalDetails toPersonalDetails(UpdatePersonalDetailsV2Request updatePersonalDetailsRequest) {
        return new PersonalDetails(
                updatePersonalDetailsRequest.getPersonalDetailsId(),
                updatePersonalDetailsRequest.getFirstName(),
                updatePersonalDetailsRequest.getLastName(),
                updatePersonalDetailsRequest.getDateOfBirth()
        );
    }
}
