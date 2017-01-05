package ch.p3n.apps.appfront.test.util;

import java.time.LocalDateTime;

/**
 * Utility class to deal with dates for testing purposes.
 *
 * @author deluc1
 * @author zempm3
 */
public class DateUtil {

    private DateUtil() {
        // private constructor as all methods are static.
    }

    public static LocalDateTime getDateInPast() {
        final LocalDateTime now = LocalDateTime.now();
        return now.minusHours(2);
    }

    public static LocalDateTime getDateInFuture() {
        final LocalDateTime now = LocalDateTime.now();
        return now.plusHours(2);
    }

}
