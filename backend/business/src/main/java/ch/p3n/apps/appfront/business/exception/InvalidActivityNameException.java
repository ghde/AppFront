package ch.p3n.apps.appfront.business.exception;

import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;

/**
 * This exception indicates that an invalid activity name received which does not match the requirements.
 *
 * @author deluc1
 * @author zempm3
 */
public class InvalidActivityNameException extends BusinessException {

    public InvalidActivityNameException() {
        super(BusinessError.INVALID_ACTIVITY_NAME);
    }

}
