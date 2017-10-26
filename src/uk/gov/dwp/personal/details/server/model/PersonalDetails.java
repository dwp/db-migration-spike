package uk.gov.dwp.personal.details.server.model;

import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;

import static uk.gov.dwp.personal.details.type.PersonalDetailsId.newPersonalDetailsId;

public class PersonalDetails {

    private final PersonalDetailsId personalDetailsId;
    private final String name;
    private final LocalDate dateOfBirth;

    public PersonalDetails(PersonalDetailsId personalDetailsId,
                           String name,
                           LocalDate dateOfBirth) {
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