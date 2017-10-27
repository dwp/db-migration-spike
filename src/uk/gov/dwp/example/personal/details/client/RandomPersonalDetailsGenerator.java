package uk.gov.dwp.example.personal.details.client;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDate;
import java.time.Month;

public class RandomPersonalDetailsGenerator {

    private static final int MAX_YEAR_OF_BIRTH = LocalDate.now().getYear();
    private static final int MIN_YEAR_OF_BIRTH = MAX_YEAR_OF_BIRTH - 110;

    private final Faker faker = new Faker();

    public String randomFirstName() {
        return faker.name().firstName();
    }

    public String randomLastName() {
        return faker.name().lastName();
    }

    public LocalDate randomDateOfBirth() {
        Month month = Month.of(RandomUtils.nextInt(1, 12));
        int year = RandomUtils.nextInt(MIN_YEAR_OF_BIRTH, MAX_YEAR_OF_BIRTH);
        return LocalDate.of(
                year,
                month,
                getRandomDayOfMonth(year, month)
        );
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

    public String randomFullName() {
        return randomFirstName() + " " + randomLastName();
    }
}
