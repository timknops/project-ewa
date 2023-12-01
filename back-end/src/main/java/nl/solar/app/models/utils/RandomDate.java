package nl.solar.app.models.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

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
}
