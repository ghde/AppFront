package ch.p3n.apps.appfront.business.exception;

import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;

/**
 * This exception indicates that an invalid interest id was received which does not match the requirements.
 *
 * @author deluc1
 * @author zempm3
 */
public class InvalidInterestIdException extends BusinessException {

    public InvalidInterestIdException() {
        super(BusinessError.INVALID_INTEREST_ID);
    }

}