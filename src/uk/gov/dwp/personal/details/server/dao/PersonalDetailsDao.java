package uk.gov.dwp.personal.details.server.dao;

import uk.gov.dwp.personal.details.api.PersonalDetails;
import uk.gov.dwp.personal.details.api.PersonalDetailsId;

public interface PersonalDetailsDao {

    void create(PersonalDetails personalDetails);

    PersonalDetails findById(PersonalDetailsId personalDetailsId);

    void update(PersonalDetails personalDetails);

    void delete(PersonalDetailsId personalDetailsId);
}
