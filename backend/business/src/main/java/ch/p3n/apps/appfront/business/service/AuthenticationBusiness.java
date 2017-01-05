package ch.p3n.apps.appfront.business.service;

import ch.p3n.apps.appfront.business.exception.InvalidClientIdException;
import ch.p3n.apps.appfront.business.exception.InvalidClientPublicKeyException;
import ch.p3n.apps.appfront.domain.entity.AuthenticationEntity;

/**
 * Describes the available services offered by the authentication business.<br>
 * This is used by rest controller and handles business logic for authentication.
 *
 * @author deluc1
 * @author zempm3
 */
public interface AuthenticationBusiness {

    /**
     * Method to create a new authentication entity based on a public key.
     *
     * @param publicKey public key of app.
     * @return authentication entity.
     * @throws InvalidClientPublicKeyException in case client public key is invalid.
     */
    AuthenticationEntity createRegistration(final String publicKey) throws InvalidClientPublicKeyException;

    /**
     * Method to get an existing authentication entity based on a client id.
     *
     * @param clientId client id.
     * @return authentication entity.
     * @throws InvalidClientIdException in case the clientId is invalid.
     */
    AuthenticationEntity getRegistration(final String clientId) throws InvalidClientIdException;

}
