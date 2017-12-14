package uk.gov.dwp.personal.details.client.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;

import static uk.gov.dwp.personal.details.type.PersonalDetailsId.newPersonalDetailsId;

public class PersonalDetailsV2Response {

    private final PersonalDetailsId personalDetailsId;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;

    PersonalDetailsV2Response(@JsonProperty("personalDetailsId") PersonalDetailsId personalDetailsId,
                              @JsonProperty("firstName") String firstName,
                              @JsonProperty("lastName") String lastName,
                              @JsonProperty("dateOfBirth") LocalDate dateOfBirth) {
        this.personalDetailsId = personalDetailsId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public PersonalDetailsId getPersonalDetailsId() {
        return personalDetailsId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public static PersonalDetailsResponseBuilder newPersonalDetailsResponse() {
        return new PersonalDetailsResponseBuilder()
                .withPersonalDetailsId(newPersonalDetailsId());
    }

    public static class PersonalDetailsResponseBuilder {

        private PersonalDetailsId personalDetailsId;
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;

        private PersonalDetailsResponseBuilder() {
        }

        public PersonalDetailsResponseBuilder withPersonalDetailsId(PersonalDetailsId personalDetailsId) {
            this.personalDetailsId = personalDetailsId;
            return this;
        }

        public PersonalDetailsResponseBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public PersonalDetailsResponseBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public PersonalDetailsResponseBuilder withDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public PersonalDetailsV2Response build() {
            return new PersonalDetailsV2Response(
                    personalDetailsId,
                    firstName,
                    lastName,
                    dateOfBirth
            );
        }
    }
}