package ch.p3n.apps.appfront.business.exception;

import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;

/**
 * This exception indicates that the client request was wrongly encrypted.
 *
 * @author deluc1
 * @author zempm3
 */
public class InvalidRequestEncryptionException extends BusinessException {

    public InvalidRequestEncryptionException() {
        super(BusinessError.INVALID_ENCRYPTION_IN_REQUEST);
    }

}
