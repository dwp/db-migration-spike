package uk.gov.dwp.personal.details.client;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsAnything;
import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import java.time.LocalDate;

public class PersonalDetailsMatcher extends TypeSafeMatcher<PersonalDetails> {

    private Matcher<PersonalDetailsId> personalDetailsIdMatcher = new IsAnything<>();
    private Matcher<String> nameMatcher = new IsAnything<>();
    private Matcher<LocalDate> dateOfBirthMatcher = new IsAnything<>();

    private PersonalDetailsMatcher() {}

    public static PersonalDetailsMatcher personalDetails() {
        return new PersonalDetailsMatcher();
    }

    public PersonalDetailsMatcher withPersonalDetailsId(Matcher<PersonalDetailsId> personalDetailsId) {
        this.personalDetailsIdMatcher = personalDetailsId;
        return this;
    }

    public PersonalDetailsMatcher withName(Matcher<String> name) {
        this.nameMatcher = name;
        return this;
    }

    public PersonalDetailsMatcher withDateOfBirth(Matcher<LocalDate> dateOfBirth) {
        this.dateOfBirthMatcher = dateOfBirth;
        return this;
    }

    @Override
    protected boolean matchesSafely(PersonalDetails personalDetails) {
        return personalDetailsIdMatcher.matches(personalDetails.getPersonalDetailsId())
                && nameMatcher.matches(personalDetails.getName())
                && dateOfBirthMatcher.matches(personalDetails.getDateOfBirth());
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("personalDetailsId=").appendDescriptionOf(personalDetailsIdMatcher)
                .appendText(" name").appendDescriptionOf(nameMatcher)
                .appendText(" dateOfBirth=").appendDescriptionOf(dateOfBirthMatcher);
    }
}