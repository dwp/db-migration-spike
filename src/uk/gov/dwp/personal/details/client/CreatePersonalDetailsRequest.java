package uk.gov.dwp.personal.details.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;

import static uk.gov.dwp.personal.details.type.PersonalDetailsId.newPersonalDetailsId;

public class CreatePersonalDetailsRequest {

    private final PersonalDetailsId personalDetailsId;
    private final String name;
    private final LocalDate dateOfBirth;

    public CreatePersonalDetailsRequest(@JsonProperty("personalDetailsId") PersonalDetailsId personalDetailsId,
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

    public static CreatePersonalDetailsRequestBuilder newPersonalDetailsRequest() {
        return new CreatePersonalDetailsRequestBuilder()
                .withPersonalDetailsId(newPersonalDetailsId());
    }

    public static class CreatePersonalDetailsRequestBuilder {

        private PersonalDetailsId personalDetailsId;
        private String name;
        private LocalDate dateOfBirth;

        private CreatePersonalDetailsRequestBuilder() {
        }

        public CreatePersonalDetailsRequestBuilder withPersonalDetailsId(PersonalDetailsId personalDetailsId) {
            this.personalDetailsId = personalDetailsId;
            return this;
        }

        public CreatePersonalDetailsRequestBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CreatePersonalDetailsRequestBuilder withDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public CreatePersonalDetailsRequest build() {
            return new CreatePersonalDetailsRequest(
                    personalDetailsId,
                    name,
                    dateOfBirth
            );
        }
    }
}