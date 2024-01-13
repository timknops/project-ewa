package nl.solar.app.models.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * test for utility class to get a random date
 *
 * @author Julian Kruithof
 */
public class RandomDateTest {
    final LocalDateTime MINIMUM_START = LocalDateTime.of(2023, 9, 12, 0, 0, 0);
    final LocalDateTime MAXIMUM_END = LocalDateTime.of(2024, 2, 1, 0, 0, 0);

    @Test
    public void testRandomLocalDateTime() {
        LocalDateTime randomDate = RandomDate.randomLocalDateTime(MINIMUM_START, MAXIMUM_END);
        assertThat(randomDate, is(greaterThanOrEqualTo(MINIMUM_START)));
        assertThat(randomDate, is(lessThanOrEqualTo(MAXIMUM_END)));
    }

    @Test
    public void testRandomLocalDate() {
        LocalDate randomDate = RandomDate.randomLocalDate(MINIMUM_START.toLocalDate(), MAXIMUM_END.toLocalDate());
        assertThat(randomDate, is(greaterThanOrEqualTo(MINIMUM_START.toLocalDate())));
        assertThat(randomDate, is(lessThanOrEqualTo(MAXIMUM_END.toLocalDate())));
    }
}
