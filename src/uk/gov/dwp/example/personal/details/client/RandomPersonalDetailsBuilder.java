package uk.gov.dwp.example.personal.details.client;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomUtils;
import uk.gov.dwp.personal.details.client.PersonalDetails;
import uk.gov.dwp.personal.details.client.PersonalDetailsId;

import java.time.LocalDate;
import java.time.Month;

public class RandomPersonalDetailsBuilder {

    private static final int MAX_YEAR_OF_BIRTH = LocalDate.now().getYear();
    private static final int MIN_YEAR_OF_BIRTH = MAX_YEAR_OF_BIRTH - 110;

    private final Faker faker = new Faker();

    private PersonalDetailsId personalDetailsId;
    private LocalDate dateOfBirth;
    private String firstName;
    private String lastName;

    private RandomPersonalDetailsBuilder() {
    }

    public static RandomPersonalDetailsBuilder withRandomPersonalDetails() {
        return new RandomPersonalDetailsBuilder()
                .withPersonalDetailsId(PersonalDetailsId.newPersonalDetailsId())
                .withRandomFirstName()
                .withRandomLastName()
                .withRandomDateOfBirth();
    }

    public RandomPersonalDetailsBuilder withPersonalDetailsId(PersonalDetailsId personalDetailsId) {
        this.personalDetailsId = personalDetailsId;
        return this;
    }

    public RandomPersonalDetailsBuilder withRandomDateOfBirth() {
        Month month = Month.of(RandomUtils.nextInt(1, 12));
        int year = RandomUtils.nextInt(MIN_YEAR_OF_BIRTH, MAX_YEAR_OF_BIRTH);
        this.dateOfBirth = LocalDate.of(
                year,
                month,
                getRandomDayOfMonth(year, month)
        );
        return this;
    }

    private int getRandomDayOfMonth(int year, Month month) {
        int days = month.maxLength();
        if ((month == Month.FEBRUARY) && !isLeapYear(year)) {
            days--;
        }
        return RandomUtils.nextInt(1, days);
    }

    private boolean isLeapYear(int year) {
        return ((year & 4) == 0) && ((year % 100) != 0 || (year % 400) == 0);
    }

    public RandomPersonalDetailsBuilder withRandomFirstName() {
        firstName = faker.name().firstName();
        return this;
    }

    public RandomPersonalDetailsBuilder withRandomLastName() {
        this.lastName = faker.name().lastName();
        return this;
    }

    public PersonalDetails build() {
        return new PersonalDetails(
                personalDetailsId,
                firstName + " "  + lastName,
                dateOfBirth
        );
    }
}
