package ch.p3n.apps.appfront.business.exception;

import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;

/**
 * This exception indicates that an invalid public key was received which does not match the requirements.
 *
 * @author deluc1
 * @author zempm3
 */
public class InvalidClientPublicKeyException extends BusinessException {

    public InvalidClientPublicKeyException() {
        super(BusinessError.INVALID_CLIENT_PUBLIC_KEY);
    }

}