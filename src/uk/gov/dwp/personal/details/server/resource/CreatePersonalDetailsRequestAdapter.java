package uk.gov.dwp.personal.details.server.resource;

import uk.gov.dwp.personal.details.client.CreatePersonalDetailsRequest;
import uk.gov.dwp.personal.details.server.migration.NameSplitter;
import uk.gov.dwp.personal.details.server.migration.NameSplitter.Names;
import uk.gov.dwp.personal.details.server.model.PersonalDetails;

public class CreatePersonalDetailsRequestAdapter {

    private final NameSplitter nameSplitter;

    public CreatePersonalDetailsRequestAdapter(NameSplitter nameSplitter) {
        this.nameSplitter = nameSplitter;
    }

    PersonalDetails toPersonalDetails(CreatePersonalDetailsRequest createPersonalDetailsRequest) {
        Names names = nameSplitter.split(createPersonalDetailsRequest.getName());
        return new PersonalDetails(
                createPersonalDetailsRequest.getPersonalDetailsId(),
                names.getFirstName(),
                names.getLastName(),
                createPersonalDetailsRequest.getDateOfBirth()
        );
    }
}
