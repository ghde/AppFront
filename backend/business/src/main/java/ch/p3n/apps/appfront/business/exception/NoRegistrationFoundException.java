package ch.p3n.apps.appfront.business.exception;

import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;

/**
 * This exception indicates that the requested client id was not found in backend data set. Client must redo registration.
 *
 * @author deluc1
 * @author zempm3
 */
public class NoRegistrationFoundException extends BusinessException {

    public NoRegistrationFoundException() {
        super(BusinessError.NO_REGISTRATION_FOUND);
    }

}
