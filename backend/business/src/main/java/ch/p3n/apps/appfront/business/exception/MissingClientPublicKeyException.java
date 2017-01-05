package ch.p3n.apps.appfront.business.exception;

import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;

/**
 * This exception indicates that the client public key is missing in request.
 *
 * @author deluc1
 * @author zempm3
 */
public class MissingClientPublicKeyException extends BusinessException {

    public MissingClientPublicKeyException() {
        super(BusinessError.MISSING_CLIENT_PUBLIC_KEY);
    }

}
