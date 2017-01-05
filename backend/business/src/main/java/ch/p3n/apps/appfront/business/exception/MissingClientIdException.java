package ch.p3n.apps.appfront.business.exception;

import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;

/**
 * This exception indicates that the client id is missing in request.
 *
 * @author deluc1
 * @author zempm3
 */
public class MissingClientIdException extends BusinessException {

    public MissingClientIdException() {
        super(BusinessError.MISSING_CLIENT_ID);
    }

}
