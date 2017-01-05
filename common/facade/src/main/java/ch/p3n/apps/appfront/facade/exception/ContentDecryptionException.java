package ch.p3n.apps.appfront.facade.exception;

/**
 * Exception which indicates that content could not be decrypted.
 *
 * @author deluc1
 */
public class ContentDecryptionException extends Exception {

    /**
     * Default constructor for {@link ContentDecryptionException}.
     * @param e wrapped exception.
     */
    public ContentDecryptionException(final Exception e) {
        super("Provided content could not be decrypted", e);
    }

}
