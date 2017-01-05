package ch.p3n.apps.appfront.api.exception;

/**
 * Represents the available business errors.
 *
 * @author deluc1
 * @author zempm3
 */
public enum BusinessError {

    NO_REGISTRATION_FOUND,
    NO_ACTIVATION_FOUND,
    INVALID_ACTIVITY_NAME,
    INVALID_ENCRYPTION_IN_REQUEST,
    INVALID_CLIENT_PUBLIC_KEY,
    INVALID_CLIENT_PUSH_TOKEN,
    INVALID_CLIENT_ID,
    INVALID_INTEREST_ID,
    MISSING_CLIENT_PUBLIC_KEY,
    MISSING_CLIENT_ID,
    MISSING_INTEREST_ID,
    UNKNOWN

}
