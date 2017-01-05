package ch.p3n.apps.appfront.facade.exception;

/**
 * Exception which indicates that content could not be encrypted.
 *
 * @author deluc1
 */
public class ContentEncryptionException extends Exception {

    /**
     * Default constructor for {@link ContentEncryptionException}.
     * @param e wrapped exception.
     */
    public ContentEncryptionException(final Exception e) {
        super("Provided content could not be encrypted", e);
    }

}
