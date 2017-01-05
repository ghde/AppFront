package ch.p3n.apps.appfront.facade.exception;

/**
 * Exception which indicates that an invalid key file was provided.
 *
 * @author deluc1
 */
public class KeyFileReadException extends Exception {

    /**
     * Constructor for {@link KeyFileReadException}.
     * @param e wrapped exception.
     */
    public KeyFileReadException(final Exception e) {
        super("An invalid key file was provided", e);
    }

}
