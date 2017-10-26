package uk.gov.dwp.personal.details.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;

import static uk.gov.dwp.personal.details.type.PersonalDetailsId.newPersonalDetailsId;

public class UpdatePersonalDetailsRequest {

    private final PersonalDetailsId personalDetailsId;
    private final String name;
    private final LocalDate dateOfBirth;

    public UpdatePersonalDetailsRequest(@JsonProperty("personalDetailsId") PersonalDetailsId personalDetailsId,
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

    public static UpdatePersonalDetailsRequestBuilder newUpdatePersonalDetailsRequest() {
        return new UpdatePersonalDetailsRequestBuilder()
                .withPersonalDetailsId(newPersonalDetailsId());
    }

    public static class UpdatePersonalDetailsRequestBuilder {

        private PersonalDetailsId personalDetailsId;
        private String name;
        private LocalDate dateOfBirth;

        private UpdatePersonalDetailsRequestBuilder() {
        }

        public UpdatePersonalDetailsRequestBuilder withPersonalDetailsId(PersonalDetailsId personalDetailsId) {
            this.personalDetailsId = personalDetailsId;
            return this;
        }

        public UpdatePersonalDetailsRequestBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public UpdatePersonalDetailsRequestBuilder withDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public UpdatePersonalDetailsRequest build() {
            return new UpdatePersonalDetailsRequest(
                    personalDetailsId,
                    name,
                    dateOfBirth
            );
        }
    }
}