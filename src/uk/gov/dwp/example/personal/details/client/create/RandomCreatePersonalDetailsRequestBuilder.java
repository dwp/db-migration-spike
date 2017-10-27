package uk.gov.dwp.example.personal.details.client.create;

import uk.gov.dwp.example.personal.details.client.RandomPersonalDetailsGenerator;
import uk.gov.dwp.personal.details.client.CreatePersonalDetailsRequest;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;

public class RandomCreatePersonalDetailsRequestBuilder {

    private final RandomPersonalDetailsGenerator personalDetailsGenerator;

    private PersonalDetailsId personalDetailsId;
    private LocalDate dateOfBirth;
    private String firstName;
    private String lastName;

    private RandomCreatePersonalDetailsRequestBuilder() {
        this.personalDetailsGenerator = new RandomPersonalDetailsGenerator();
    }

    public static RandomCreatePersonalDetailsRequestBuilder newRandomCreatePersonalDetailsRequest() {
        return new RandomCreatePersonalDetailsRequestBuilder()
                .withPersonalDetailsId(PersonalDetailsId.newPersonalDetailsId())
                .withRandomFirstName()
                .withRandomLastName()
                .withRandomDateOfBirth();
    }

    public RandomCreatePersonalDetailsRequestBuilder withPersonalDetailsId(PersonalDetailsId personalDetailsId) {
        this.personalDetailsId = personalDetailsId;
        return this;
    }

    public RandomCreatePersonalDetailsRequestBuilder withRandomDateOfBirth() {
        this.dateOfBirth = personalDetailsGenerator.randomDateOfBirth();
        return this;
    }

    public RandomCreatePersonalDetailsRequestBuilder withRandomFirstName() {
        firstName = personalDetailsGenerator.randomFirstName();
        return this;
    }

    public RandomCreatePersonalDetailsRequestBuilder withRandomLastName() {
        this.lastName = personalDetailsGenerator.randomLastName();
        return this;
    }

    public CreatePersonalDetailsRequest build() {
        return new CreatePersonalDetailsRequest(
                personalDetailsId,
                firstName + " "  + lastName,
                dateOfBirth
        );
    }
}
