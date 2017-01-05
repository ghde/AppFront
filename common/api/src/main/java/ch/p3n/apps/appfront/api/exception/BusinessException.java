package ch.p3n.apps.appfront.api.exception;

/**
 * Abstract business exception. All exception thrown by the api extend this class.
 *
 * @author deluc1
 * @author zempm3
 */
public class BusinessException extends Exception {

    private final BusinessError error;

    /**
     * Default constructor for {@link BusinessException}.
     * @param error wrapped business error.
     */
    public BusinessException(final BusinessError error) {
        super(error.name());
        this.error = error;
    }

    public BusinessError getError() {
        return error;
    }

    @Override
    public String toString() {
        return String.format("#ERROR#%s#", error.name());
    }

}
