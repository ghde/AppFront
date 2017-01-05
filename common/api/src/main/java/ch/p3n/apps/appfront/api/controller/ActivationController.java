package ch.p3n.apps.appfront.api.controller;

import ch.p3n.apps.appfront.api.dto.ActivationDTO;
import ch.p3n.apps.appfront.api.dto.InterestDTO;
import ch.p3n.apps.appfront.api.exception.BusinessException;

/**
 * Describes the available services offered by the activation controller.<br>
 * This is used by the "Appfront" app to activate and deactivate visibility.
 *
 * @author deluc1
 * @author zempm3
 */
public interface ActivationController {

    /**
     * Method to register a new client app.
     *
     * <pre>
     *     <strong>Request URL:</strong>
     *     <code>POST: /activate</code>
     *     <strong>Request Body:</strong>
     *     <code>ActivationDTO</code>
     *     <strong>Response:</strong>
     *     <code>InterestDTO</code>
     * </pre>
     *
     * @param activationData activationData.
     * @return interest data.
     * @throws BusinessException in case a business exception occurs.
     */
    InterestDTO postActivate(final ActivationDTO activationData) throws BusinessException;

    /**
     * Method to register a new client app.
     *
     * <pre>
     *     <strong>Request URL:</strong>
     *     <code>POST: /activate</code>
     *     <strong>Request Body:</strong>
     *     <code>ActivationDTO</code>
     *     <strong>Response:</strong>
     *     <code>-</code>
     * </pre>
     *
     * @param activationData activation data.
     * @throws BusinessException in case a business exception occurs.
     */
    void postDeactivate(final ActivationDTO activationData) throws BusinessException;


}
