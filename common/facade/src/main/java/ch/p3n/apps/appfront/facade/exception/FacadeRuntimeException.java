package ch.p3n.apps.appfront.facade.exception;

import ch.p3n.apps.appfront.api.exception.BusinessError;

/**
 * Runtime exception for errors catched in facade http call.
 *
 * @author deluc1
 * @author zempm3
 */
public class FacadeRuntimeException extends RuntimeException {

    private final BusinessError error;

    /**
     * Constructor for {@link FacadeRuntimeException}.
     * @param error business error.
     */
    public FacadeRuntimeException(final BusinessError error) {
        this.error = error;
    }

    public BusinessError getError() {
        return error;
    }

}
