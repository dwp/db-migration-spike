package uk.gov.dwp.personal.details.server.model;

import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;

import static uk.gov.dwp.personal.details.type.PersonalDetailsId.newPersonalDetailsId;

public class PersonalDetails {

    private final PersonalDetailsId personalDetailsId;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;

    public PersonalDetails(PersonalDetailsId personalDetailsId,
                           String firstName,
                           String lastName,
                           LocalDate dateOfBirth) {
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

    public static PersonalDetailsBuilder newPersonalDetails() {
        return new PersonalDetailsBuilder()
                .withPersonalDetailsId(newPersonalDetailsId());
    }

    public static class PersonalDetailsBuilder {

        private PersonalDetailsId personalDetailsId;
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;

        private PersonalDetailsBuilder() {
        }

        public PersonalDetailsBuilder withPersonalDetailsId(PersonalDetailsId personalDetailsId) {
            this.personalDetailsId = personalDetailsId;
            return this;
        }

        public PersonalDetailsBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public PersonalDetailsBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public PersonalDetailsBuilder withDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public PersonalDetails build() {
            return new PersonalDetails(
                    personalDetailsId,
                    firstName,
                    lastName,
                    dateOfBirth
            );
        }
    }
}