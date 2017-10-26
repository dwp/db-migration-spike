package uk.gov.dwp.personal.details.server.dao;

import uk.gov.dwp.personal.details.client.PersonalDetails;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

public interface PersonalDetailsDao {

    void create(PersonalDetails personalDetails);

    PersonalDetails findById(PersonalDetailsId personalDetailsId);

    void update(PersonalDetails personalDetails);

    void delete(PersonalDetailsId personalDetailsId);
}
