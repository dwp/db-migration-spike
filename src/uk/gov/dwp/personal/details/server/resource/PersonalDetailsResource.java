package uk.gov.dwp.personal.details.server.resource;

import uk.gov.dwp.api.PersonalDetails;
import uk.gov.dwp.api.PersonalDetailsClient;
import uk.gov.dwp.api.PersonalDetailsId;
import uk.gov.dwp.personal.details.server.dao.PersonalDetailsDao;

public class PersonalDetailsResource implements PersonalDetailsClient {

    private final PersonalDetailsDao personalDetailsDao;

    public PersonalDetailsResource(PersonalDetailsDao personalDetailsDao) {
        this.personalDetailsDao = personalDetailsDao;
    }

    @Override
    public PersonalDetails findById(PersonalDetailsId personalDetailsId) {
        return personalDetailsDao.findById(personalDetailsId);
    }

    @Override
    public void create(PersonalDetails personalDetails) {
        personalDetailsDao.create(personalDetails);
    }

    @Override
    public void update(PersonalDetails personalDetails) {
        personalDetailsDao.update(personalDetails);
    }

    @Override
    public void delete(PersonalDetailsId personalDetailsId) {
        personalDetailsDao.delete(personalDetailsId);
    }
}
