package uk.gov.dwp.personal.details.server.resource;

import uk.gov.dwp.personal.details.client.CreatePersonalDetailsRequest;
import uk.gov.dwp.personal.details.client.PersonalDetails;
import uk.gov.dwp.personal.details.client.PersonalDetailsClient;
import uk.gov.dwp.personal.details.client.UpdatePersonalDetailsRequest;
import uk.gov.dwp.personal.details.server.dao.PersonalDetailsDao;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

public class PersonalDetailsResource implements PersonalDetailsClient {

    private final PersonalDetailsDao personalDetailsDao;
    private final CreatePersonalDetailsRequestAdapter createPersonalDetailsRequestAdapter;
    private final UpdatePersonalDetailsRequestAdapter updatePersonalDetailsRequestAdapter;

    public PersonalDetailsResource(PersonalDetailsDao personalDetailsDao,
                                   CreatePersonalDetailsRequestAdapter createPersonalDetailsRequestAdapter,
                                   UpdatePersonalDetailsRequestAdapter updatePersonalDetailsRequestAdapter) {
        this.personalDetailsDao = personalDetailsDao;
        this.createPersonalDetailsRequestAdapter = createPersonalDetailsRequestAdapter;
        this.updatePersonalDetailsRequestAdapter = updatePersonalDetailsRequestAdapter;
    }

    @Override
    public PersonalDetails findById(PersonalDetailsId personalDetailsId) {
        return personalDetailsDao.findById(personalDetailsId);
    }

    @Override
    public void create(CreatePersonalDetailsRequest createPersonalDetailsRequest) {
        personalDetailsDao.create(createPersonalDetailsRequestAdapter.toPersonalDetails(createPersonalDetailsRequest));
    }

    @Override
    public void update(UpdatePersonalDetailsRequest updatePersonalDetailsRequest) {
        personalDetailsDao.update(updatePersonalDetailsRequestAdapter.toPersonalDetails(updatePersonalDetailsRequest));
    }

    @Override
    public void delete(PersonalDetailsId personalDetailsId) {
        personalDetailsDao.delete(personalDetailsId);
    }
}
