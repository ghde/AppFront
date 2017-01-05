package ch.p3n.apps.appfront.business.exception;

import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;

/**
 * This exception indicates that the requested interest id was not found in backend data set.
 *
 * @author deluc1
 * @author zempm3
 */
public class NoActivationFoundException extends BusinessException {

    public NoActivationFoundException() {
        super(BusinessError.NO_ACTIVATION_FOUND);
    }

}
