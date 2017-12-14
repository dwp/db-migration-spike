package uk.gov.dwp.personal.details.client.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;

import static uk.gov.dwp.personal.details.type.PersonalDetailsId.newPersonalDetailsId;

public class CreatePersonalDetailsV2Request {

    private final PersonalDetailsId personalDetailsId;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;

    public CreatePersonalDetailsV2Request(@JsonProperty("personalDetailsId") PersonalDetailsId personalDetailsId,
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

    public static CreatePersonalDetailsRequestBuilder newPersonalDetailsRequest() {
        return new CreatePersonalDetailsRequestBuilder()
                .withPersonalDetailsId(newPersonalDetailsId());
    }

    public static class CreatePersonalDetailsRequestBuilder {

        private PersonalDetailsId personalDetailsId;
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;

        private CreatePersonalDetailsRequestBuilder() {
        }

        public CreatePersonalDetailsRequestBuilder withPersonalDetailsId(PersonalDetailsId personalDetailsId) {
            this.personalDetailsId = personalDetailsId;
            return this;
        }

        public CreatePersonalDetailsRequestBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CreatePersonalDetailsRequestBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CreatePersonalDetailsRequestBuilder withDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public CreatePersonalDetailsV2Request build() {
            return new CreatePersonalDetailsV2Request(
                    personalDetailsId,
                    firstName,
                    lastName,
                    dateOfBirth
            );
        }
    }
}