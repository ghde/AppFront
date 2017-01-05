package ch.p3n.apps.appfront.business.service.impl;

import ch.p3n.apps.appfront.business.exception.InvalidClientIdException;
import ch.p3n.apps.appfront.business.exception.InvalidClientPublicKeyException;
import ch.p3n.apps.appfront.business.service.AuthenticationBusiness;
import ch.p3n.apps.appfront.domain.entity.AuthenticationEntity;
import ch.p3n.apps.appfront.domain.service.AuthenticationDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Default implementation of {@link AuthenticationBusiness}.
 *
 * @author deluc1
 * @author zempm3
 */
@Component
public class AuthenticationBusinessImpl extends AbstractBusiness implements AuthenticationBusiness {

    @Autowired
    private AuthenticationDbService authenticationDbService;

    @Override
    public AuthenticationEntity createRegistration(final String publicKey) throws InvalidClientPublicKeyException {
        validateClientPublicKey(publicKey);

        final AuthenticationEntity authenticationEntity = new AuthenticationEntity();
        authenticationEntity.setClientId(UUID.randomUUID().toString());
        authenticationEntity.setClientPublicKey(publicKey);
        return authenticationDbService.createAuthentication(authenticationEntity);
    }

    @Override
    public AuthenticationEntity getRegistration(final String clientId) throws InvalidClientIdException {
        validateClientId(clientId);

        return authenticationDbService.getAuthenticationByClientId(clientId);
    }

}
