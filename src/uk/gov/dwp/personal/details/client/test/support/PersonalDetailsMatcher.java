package uk.gov.dwp.personal.details.client;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsAnything;
import uk.gov.dwp.personal.details.server.model.PersonalDetails;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;

public class PersonalDetailsMatcher extends TypeSafeMatcher<PersonalDetails> {

    private Matcher<PersonalDetailsId> personalDetailsIdMatcher = new IsAnything<>();
    private Matcher<String> firstNameMatcher = new IsAnything<>();
    private Matcher<String> lastNameMatcher = new IsAnything<>();
    private Matcher<LocalDate> dateOfBirthMatcher = new IsAnything<>();

    private PersonalDetailsMatcher() {}

    public static PersonalDetailsMatcher personalDetails() {
        return new PersonalDetailsMatcher();
    }

    public PersonalDetailsMatcher withPersonalDetailsId(Matcher<PersonalDetailsId> personalDetailsId) {
        this.personalDetailsIdMatcher = personalDetailsId;
        return this;
    }

    public PersonalDetailsMatcher withFirstName(Matcher<String> firstName) {
        this.firstNameMatcher = firstName;
        return this;
    }

    public PersonalDetailsMatcher withLastName(Matcher<String> lastName) {
        this.lastNameMatcher = lastName;
        return this;
    }

    public PersonalDetailsMatcher withDateOfBirth(Matcher<LocalDate> dateOfBirth) {
        this.dateOfBirthMatcher = dateOfBirth;
        return this;
    }

    @Override
    protected boolean matchesSafely(PersonalDetails personalDetails) {
        return personalDetailsIdMatcher.matches(personalDetails.getPersonalDetailsId())
                && firstNameMatcher.matches(personalDetails.getFirstName())
                && lastNameMatcher.matches(personalDetails.getLastName())
                && dateOfBirthMatcher.matches(personalDetails.getDateOfBirth());
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("personalDetailsId=").appendDescriptionOf(personalDetailsIdMatcher)
                .appendText(" firstName").appendDescriptionOf(firstNameMatcher)
                .appendText(" lastName").appendDescriptionOf(lastNameMatcher)
                .appendText(" dateOfBirth=").appendDescriptionOf(dateOfBirthMatcher);
    }
}