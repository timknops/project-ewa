package nl.solar.app.models.utils;

import java.time.*;

/**
 * Utility class to get a random date
 *
 * @author Julian Kruithof
 */
public class RandomDate {

    public static LocalDateTime randomLocalDateTime(LocalDateTime start, LocalDateTime end) {

        long startEpochSecond = start.toEpochSecond(ZoneOffset.UTC);
        long endEpochSeconds = end.toEpochSecond(ZoneOffset.UTC);

        long randomSeconds = (long) Math.floor(Math.random() *(endEpochSeconds - startEpochSecond) + startEpochSecond);

        return LocalDateTime.ofEpochSecond(randomSeconds, 0,
                ZoneId.systemDefault().getRules().getOffset(Instant.ofEpochSecond(randomSeconds)));
    }

    public static LocalDate randomLocalDate(LocalDate start, LocalDate end) {
        long startEpoch= start.toEpochDay();
        long endEpoch = end.toEpochDay();

        long randomDay = (long) Math.floor(Math.random() * (endEpoch - startEpoch) + startEpoch);

        return LocalDate.ofEpochDay(randomDay);
    }
}
