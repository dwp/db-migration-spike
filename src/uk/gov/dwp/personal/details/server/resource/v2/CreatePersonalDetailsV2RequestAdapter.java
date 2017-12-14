package uk.gov.dwp.personal.details.server.resource.v2;

import uk.gov.dwp.personal.details.client.v2.CreatePersonalDetailsV2Request;
import uk.gov.dwp.personal.details.server.model.PersonalDetails;

public class CreatePersonalDetailsV2RequestAdapter {

    PersonalDetails toPersonalDetails(CreatePersonalDetailsV2Request createPersonalDetailsRequest) {
        return new PersonalDetails(
                createPersonalDetailsRequest.getPersonalDetailsId(),
                createPersonalDetailsRequest.getFirstName(),
                createPersonalDetailsRequest.getLastName(),
                createPersonalDetailsRequest.getDateOfBirth()
        );
    }
}
