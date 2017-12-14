package uk.gov.dwp.personal.details.server.resource;

import uk.gov.dwp.personal.details.client.UpdatePersonalDetailsRequest;
import uk.gov.dwp.personal.details.server.migration.NameSplitter;
import uk.gov.dwp.personal.details.server.migration.NameSplitter.Names;
import uk.gov.dwp.personal.details.server.model.PersonalDetails;

public class UpdatePersonalDetailsRequestAdapter {

    private final NameSplitter nameSplitter;

    public UpdatePersonalDetailsRequestAdapter(NameSplitter nameSplitter) {
        this.nameSplitter = nameSplitter;
    }

    PersonalDetails toPersonalDetails(UpdatePersonalDetailsRequest updatePersonalDetailsRequest) {
        Names names = nameSplitter.split(updatePersonalDetailsRequest.getFirstName());
        return new PersonalDetails(
                updatePersonalDetailsRequest.getPersonalDetailsId(),
                names.getFirstName(),
                names.getLastName(),
                updatePersonalDetailsRequest.getDateOfBirth()
        );
    }
}
