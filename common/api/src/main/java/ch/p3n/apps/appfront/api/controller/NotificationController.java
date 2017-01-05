package ch.p3n.apps.appfront.api.controller;

import ch.p3n.apps.appfront.api.dto.ActivationDTO;
import ch.p3n.apps.appfront.api.dto.MatchDTO;
import ch.p3n.apps.appfront.api.exception.BusinessException;

import java.util.Collection;

/**
 * Describes the available services offered by the notification controller.<br>
 * This is used by the "Appfront" app to notify backend and get notification data.
 *
 * @author deluc1
 * @author zempm3
 */
public interface NotificationController {

    /**
     * Method to register a new client app.
     *
     * <pre>
     *     <strong>Request URL:</strong>
     *     <code>POST: /notify/{otherClientInterestId}</code>
     *     <strong>Request Body:</strong>
     *     <code>ActivationDTO</code>
     *     <strong>Response:</strong>
     *     <code>-</code>
     * </pre>
     *
     * @param activationData        activation data.
     * @param otherClientInterestId other client interest id.
     * @throws BusinessException in case a business exception occurs.
     */
    void postNotify(final ActivationDTO activationData, final String otherClientInterestId) throws BusinessException;

    /**
     * Method to register a new client app.
     *
     * <pre>
     *     <strong>Request URL:</strong>
     *     <code>POST: /notifications</code>
     *     <strong>Request Body:</strong>
     *     <code>ActivationDTO</code>
     *     <strong>Response:</strong>
     *     <code>Collection of MatchDTO</code>
     * </pre>
     *
     * @param activationData activation data.
     * @return match data.
     * @throws BusinessException in case a business exception occurs.
     */
    Collection<MatchDTO> postNotifications(final ActivationDTO activationData) throws BusinessException;

}
