package ch.p3n.apps.appfront.api.controller;

import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
import ch.p3n.apps.appfront.api.exception.BusinessException;

/**
 * Describes the available services offered by the registration controller.<br>
 * This is used by the "Appfront" app to register a new customer.
 *
 * @author deluc1
 * @author zempm3
 */
@FunctionalInterface
public interface RegistrationController {

    /**
     * Method to register a new client app.
     *
     * <pre>
     *     <strong>Request URL:</strong>
     *     <code>POST: /register</code>
     *     <strong>Request Body:</strong>
     *     <code>AuthenticationDTO</code>
     *     <strong>Response:</strong>
     *     <code>AuthenticationDTO</code>
     * </pre>
     *
     * @param authenticationData authentication data.
     * @return updated authentication data.
     * @throws BusinessException in case a business exception occurs.
     */
    AuthenticationDTO postRegister(final AuthenticationDTO authenticationData) throws BusinessException;

}
