package uk.gov.dwp.personal.details.client.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;

import static uk.gov.dwp.personal.details.type.PersonalDetailsId.newPersonalDetailsId;

public class UpdatePersonalDetailsV2Request {

    private final PersonalDetailsId personalDetailsId;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;

    public UpdatePersonalDetailsV2Request(@JsonProperty("personalDetailsId") PersonalDetailsId personalDetailsId,
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

    public static UpdatePersonalDetailsRequestBuilder newUpdatePersonalDetailsRequest() {
        return new UpdatePersonalDetailsRequestBuilder()
                .withPersonalDetailsId(newPersonalDetailsId());
    }

    public static class UpdatePersonalDetailsRequestBuilder {

        private PersonalDetailsId personalDetailsId;
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;

        private UpdatePersonalDetailsRequestBuilder() {
        }

        public UpdatePersonalDetailsRequestBuilder withPersonalDetailsId(PersonalDetailsId personalDetailsId) {
            this.personalDetailsId = personalDetailsId;
            return this;
        }

        public UpdatePersonalDetailsRequestBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UpdatePersonalDetailsRequestBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UpdatePersonalDetailsRequestBuilder withDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public UpdatePersonalDetailsV2Request build() {
            return new UpdatePersonalDetailsV2Request(
                    personalDetailsId,
                    firstName,
                    lastName,
                    dateOfBirth
            );
        }
    }
}