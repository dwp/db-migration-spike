package uk.gov.dwp.personal.details.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;

import static uk.gov.dwp.personal.details.type.PersonalDetailsId.newPersonalDetailsId;

public class PersonalDetailsResponse {

    private final PersonalDetailsId personalDetailsId;
    private final String name;
    private final LocalDate dateOfBirth;

    PersonalDetailsResponse(@JsonProperty("personalDetailsId") PersonalDetailsId personalDetailsId,
                            @JsonProperty("name") String name,
                            @JsonProperty("dateOfBirth") LocalDate dateOfBirth) {
        this.personalDetailsId = personalDetailsId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public PersonalDetailsId getPersonalDetailsId() {
        return personalDetailsId;
    }

    public String getName() {
        return name;
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
        private String name;
        private LocalDate dateOfBirth;

        private PersonalDetailsResponseBuilder() {
        }

        public PersonalDetailsResponseBuilder withPersonalDetailsId(PersonalDetailsId personalDetailsId) {
            this.personalDetailsId = personalDetailsId;
            return this;
        }

        public PersonalDetailsResponseBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public PersonalDetailsResponseBuilder withDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public PersonalDetailsResponse build() {
            return new PersonalDetailsResponse(
                    personalDetailsId,
                    name,
                    dateOfBirth
            );
        }
    }
}