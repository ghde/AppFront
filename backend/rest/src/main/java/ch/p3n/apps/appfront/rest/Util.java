package ch.p3n.apps.appfront.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Utility class.
 *
 * @author deluc1
 * @author zempm3
 */
public final class Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

    private Util() {
        // Empty constructor
    }

    /**
     * Checks if the provided string is a valid UUID.
     *
     * @param uuidString uuid string.
     * @return {@code true} if string is a valid UUID. {@code false} otherwise.
     */
    public static boolean isUUID(final String uuidString) {
        try {
            UUID.fromString(uuidString);
            return true;
        } catch (IllegalArgumentException e) {
            LOGGER.debug("invalid UUID", e);
            return false;
        }
    }

}
