package ch.p3n.apps.appfront.facade.exception;

import ch.p3n.apps.appfront.api.exception.BusinessException;

/**
 * Handler which translates {@link FacadeRuntimeException} into {@link BusinessException}.
 *
 * @author deluc1
 * @author zempm3
 */
public class FacadeErrorHandler {

    private FacadeErrorHandler() {
        // Only private constructor as only contains static methods.
    }

    /**
     * Method to handle {@link FacadeRuntimeException}.
     * @param e the exception to be handled.
     * @return the business exception.
     */
    public static BusinessException handle(final FacadeRuntimeException e) {
        return new BusinessException(e.getError());
    }

}
