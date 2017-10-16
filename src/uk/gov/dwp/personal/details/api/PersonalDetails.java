package uk.gov.dwp.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

import static uk.gov.dwp.api.PersonalDetailsId.newPersonalDetailsId;

public class PersonalDetails {

    private final PersonalDetailsId personalDetailsId;
    private final String name;
    private final LocalDate dateOfBirth;

    public PersonalDetails(@JsonProperty("personalDetailsId") PersonalDetailsId personalDetailsId,
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

    public static PersonalDetailsBuilder newPersonalDetails() {
        return new PersonalDetailsBuilder()
                .withPersonalDetailsId(newPersonalDetailsId());
    }

    public static class PersonalDetailsBuilder {

        private PersonalDetailsId personalDetailsId;
        private String name;
        private LocalDate dateOfBirth;

        private PersonalDetailsBuilder() {
        }

        public PersonalDetailsBuilder withPersonalDetailsId(PersonalDetailsId personalDetailsId) {
            this.personalDetailsId = personalDetailsId;
            return this;
        }

        public PersonalDetailsBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public PersonalDetailsBuilder withDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public PersonalDetails build() {
            return new PersonalDetails(
                    personalDetailsId,
                    name,
                    dateOfBirth
            );
        }
    }
}