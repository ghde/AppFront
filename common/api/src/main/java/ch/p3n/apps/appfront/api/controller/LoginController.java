package ch.p3n.apps.appfront.api.controller;

import ch.p3n.apps.appfront.api.dto.ActivityDTO;
import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
import ch.p3n.apps.appfront.api.exception.BusinessException;

import java.util.Collection;

/**
 * Describes the available services offered by the login controller.<br>
 * This is used by the "Appfront" app to login customer which opened the app.
 *
 * @author deluc1
 * @author zempm3
 */
@FunctionalInterface
public interface LoginController {

    /**
     * Method to register a new client app.
     *
     * <pre>
     *     <strong>Request URL:</strong>
     *     <code>POST: /login</code>
     *     <strong>Request Body:</strong>
     *     <code>AuthenticationDTO</code>
     *     <strong>Response:</strong>
     *     <code>Collection of ActivityDTO</code>
     * </pre>
     *
     * @param authenticationData authentication data.
     * @return interest data.
     * @throws BusinessException in case a business exception occurs.
     */
    Collection<ActivityDTO> postLogin(final AuthenticationDTO authenticationData) throws BusinessException;

}
