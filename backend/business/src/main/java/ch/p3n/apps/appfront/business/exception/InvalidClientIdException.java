package ch.p3n.apps.appfront.business.exception;

import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;

/**
 * This exception indicates that an invalid client id was received which does not match the requirements.
 *
 * @author deluc1
 * @author zempm3
 */
public class InvalidClientIdException extends BusinessException {

    public InvalidClientIdException() {
        super(BusinessError.INVALID_CLIENT_ID);
    }

}