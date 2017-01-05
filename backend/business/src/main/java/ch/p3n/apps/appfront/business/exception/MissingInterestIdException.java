package ch.p3n.apps.appfront.business.exception;

import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;

/**
 * This exception indicates that the interest id is missing in request.
 *
 * @author deluc1
 * @author zempm3
 */
public class MissingInterestIdException extends BusinessException {

    public MissingInterestIdException() {
        super(BusinessError.MISSING_INTEREST_ID);
    }

}
