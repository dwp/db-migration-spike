package uk.gov.dwp.personal.details.server.resource.v2;

import uk.gov.dwp.personal.details.client.v2.CreatePersonalDetailsV2Request;
import uk.gov.dwp.personal.details.client.v2.PersonalDetailsV2Client;
import uk.gov.dwp.personal.details.client.v2.PersonalDetailsV2Response;
import uk.gov.dwp.personal.details.client.v2.UpdatePersonalDetailsV2Request;
import uk.gov.dwp.personal.details.server.dao.PersonalDetailsDao;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

public class PersonalDetailsV2Resource implements PersonalDetailsV2Client {

    private final PersonalDetailsDao personalDetailsDao;
    private final PersonalDetailsV2ResponseAdapter personalDetailsResponseAdapter;
    private final CreatePersonalDetailsV2RequestAdapter createPersonalDetailsV2RequestAdapter;
    private final UpdatePersonalDetailsV2RequestAdapter updatePersonalDetailsRequestAdapter;

    public PersonalDetailsV2Resource(PersonalDetailsDao personalDetailsDao,
                                     PersonalDetailsV2ResponseAdapter personalDetailsV2ResponseAdapter,
                                     CreatePersonalDetailsV2RequestAdapter createPersonalDetailsV2RequestAdapter,
                                     UpdatePersonalDetailsV2RequestAdapter updatePersonalDetailsV2RequestAdapter) {
        this.personalDetailsDao = personalDetailsDao;
        this.personalDetailsResponseAdapter = personalDetailsV2ResponseAdapter;
        this.createPersonalDetailsV2RequestAdapter = createPersonalDetailsV2RequestAdapter;
        this.updatePersonalDetailsRequestAdapter = updatePersonalDetailsV2RequestAdapter;
    }

    @Override
    public PersonalDetailsV2Response findById(PersonalDetailsId personalDetailsId) {
        return personalDetailsResponseAdapter.toPersonalDetails(personalDetailsDao.findById(personalDetailsId));
    }

    @Override
    public void create(CreatePersonalDetailsV2Request createPersonalDetailsRequest) {
        personalDetailsDao.create(createPersonalDetailsV2RequestAdapter.toPersonalDetails(createPersonalDetailsRequest));
    }

    @Override
    public void update(UpdatePersonalDetailsV2Request updatePersonalDetailsRequest) {
        personalDetailsDao.update(updatePersonalDetailsRequestAdapter.toPersonalDetails(updatePersonalDetailsRequest));
    }

    @Override
    public void delete(PersonalDetailsId personalDetailsId) {
        personalDetailsDao.delete(personalDetailsId);
    }
}
