package ru.netology.diploma;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private static Faker faker = new Faker(new Locale("en"));

    private DataHelper() {
    }

    @Value
    public static class CardInfo {
        String number;
        String month;
        String year;
        String owner;
        String cvc;
    }

    public static CardInfo getApprovedCard() {
        return new CardInfo("4444 4444 4444 4441", generateMonth(0), generateYear(1), faker.name().fullName(), "123");
    }

    public static CardInfo getDeclinedCard() {
        return new CardInfo("4444 4444 4444 4442", generateMonth(0), generateYear(1), faker.name().fullName(), "123");
    }

    public static String getInvalidOwnerRu() {
        Faker fakerRu = new Faker(new Locale("ru"));
        return fakerRu.name().fullName();
    }

    public static String generateMonth(int shift) {
        return LocalDate.now().plusMonths(shift).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String generateYear(int shift) {
        return LocalDate.now().plusYears(shift).format(DateTimeFormatter.ofPattern("yy"));
    }
}
