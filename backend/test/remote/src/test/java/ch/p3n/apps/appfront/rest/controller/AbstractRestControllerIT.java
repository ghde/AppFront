package ch.p3n.apps.appfront.rest.controller;

import ch.p3n.apps.appfront.api.dto.*;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.business.security.TestingSecurityBusiness;
import ch.p3n.apps.appfront.facade.controller.ActivationControllerFacade;
import ch.p3n.apps.appfront.facade.controller.LoginControllerFacade;
import ch.p3n.apps.appfront.facade.controller.NotificationControllerFacade;
import ch.p3n.apps.appfront.facade.controller.RegistrationControllerFacade;
import ch.p3n.apps.appfront.facade.exception.ContentDecryptionException;
import ch.p3n.apps.appfront.facade.exception.ContentEncryptionException;
import ch.p3n.apps.appfront.facade.security.DecryptionUtil;
import ch.p3n.apps.appfront.facade.security.EncryptionUtil;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import ch.p3n.apps.appfront.test.facade.ActivationControllerTestFacade;
import ch.p3n.apps.appfront.test.facade.LoginControllerTestFacade;
import ch.p3n.apps.appfront.test.facade.NotificationControllerTestFacade;
import ch.p3n.apps.appfront.test.facade.RegistrationControllerTestFacade;
import ch.p3n.apps.appfront.test.util.TestUtil;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Abstract class which contains helper methods for api interaction.
 *
 * @author zempm3
 * @author deluc1
 */
public class AbstractRestControllerIT {

    protected static final RegistrationControllerFacade REGISTER_FACADE;

    protected static final LoginControllerFacade LOGIN_FACADE;

    protected static final ActivationControllerFacade ACTIVATION_FACADE;

    protected static final NotificationControllerFacade NOTIFICATION_FACADE;

    private static PublicKey BACKEND_PUBLIC_KEY;

    static {

        try {
            BACKEND_PUBLIC_KEY = KeyGeneratorUtil.getPublicKey(TestingSecurityBusiness.PUBLIC_KEY_STRING);
        } catch (InvalidKeySpecException e) {
            // Ignore this error.
        }

        REGISTER_FACADE = new RegistrationControllerTestFacade();
        LOGIN_FACADE = new LoginControllerTestFacade();
        ACTIVATION_FACADE = new ActivationControllerTestFacade();
        NOTIFICATION_FACADE = new NotificationControllerTestFacade();
    }

    protected AuthenticationDTO postRegister(final KeyPair keyPair) throws BusinessException {
        final String publicKeyString = KeyGeneratorUtil.getKeyAsString(keyPair.getPublic());
        final AuthenticationDTO authenticationDTO = TestUtil.createAuthenticationDTO(publicKeyString);
        return postRegister(authenticationDTO);
    }

    protected AuthenticationDTO postRegisterAndDecrypt(final KeyPair keyPair) throws BusinessException, ContentDecryptionException {
        final AuthenticationDTO registeredAuthenticationDTO = postRegister(keyPair);
        return decrypt(keyPair, registeredAuthenticationDTO);
    }

    protected AuthenticationDTO postRegister(final AuthenticationDTO authenticationDTO) throws BusinessException {
        return REGISTER_FACADE.postRegister(authenticationDTO);
    }

    protected AuthenticationDTO postRegisterAndDecrypt(final KeyPair keyPair, final AuthenticationDTO authenticationDTO) throws BusinessException, ContentDecryptionException {
        final AuthenticationDTO registeredAuthenticationDTO = postRegister(authenticationDTO);
        return decrypt(keyPair, registeredAuthenticationDTO);
    }

    protected Collection<ActivityDTO> postLogin() throws ContentEncryptionException, BusinessException {
        return postLogin(UUID.randomUUID().toString());
    }

    protected Collection<ActivityDTO> postLogin(final String clientId) throws BusinessException, ContentEncryptionException {
        final AuthenticationDTO authenticationDtoForLogin = new AuthenticationDTO();
        authenticationDtoForLogin.setClientId(clientId);
        return postLogin(authenticationDtoForLogin);
    }

    protected Collection<ActivityDTO> postLoginAndDecrypt(final KeyPair keyPair, final String clientId) throws BusinessException, ContentEncryptionException {
        final AuthenticationDTO authenticationDtoForLogin = new AuthenticationDTO();
        authenticationDtoForLogin.setClientId(clientId);
        final Collection<ActivityDTO> encryptedActivities = postLogin(authenticationDtoForLogin);
        final Collection<ActivityDTO> decryptedActivities = new ArrayList<>();
        encryptedActivities.stream().forEach(activity -> {
            try {
                decryptedActivities.add(decrypt(keyPair, activity));
            } catch (ContentDecryptionException e) {
                // Ignore this case.
            }
        });
        return decryptedActivities;
    }

    protected Collection<ActivityDTO> postLogin(final AuthenticationDTO authenticationDTO) throws BusinessException, ContentEncryptionException {
        return LOGIN_FACADE.postLogin(encrypt(authenticationDTO));
    }

    protected InterestDTO postActivate(final ActivationDTO activationDTO) throws BusinessException, ContentEncryptionException {
        return ACTIVATION_FACADE.postActivate(encrypt(activationDTO));
    }

    protected InterestDTO postActivateAndDecrypt(final KeyPair keyPair, final ActivationDTO activationDTO) throws BusinessException, ContentDecryptionException, ContentEncryptionException {
        return decrypt(keyPair, postActivate(activationDTO));
    }

    protected void postDeactivate(final ActivationDTO activationDTO) throws BusinessException, ContentEncryptionException {
        ACTIVATION_FACADE.postDeactivate(encrypt(activationDTO));
    }

    protected void postNotify(final ActivationDTO activationDTO, final String otherClientInterestId) throws BusinessException, ContentEncryptionException {
        NOTIFICATION_FACADE.postNotify(encrypt(activationDTO), otherClientInterestId);
    }

    protected Collection<MatchDTO> postNotifications(final ActivationDTO activationDTO) throws BusinessException {
        return NOTIFICATION_FACADE.postNotifications(activationDTO);
    }

    protected ActivationDTO decrypt(final KeyPair keyPair, final ActivationDTO activationDTO) throws ContentDecryptionException {
        return DecryptionUtil.decrypt(keyPair.getPrivate(), activationDTO);
    }

    protected ActivityDTO decrypt(final KeyPair keyPair, final ActivityDTO activityDTO) throws ContentDecryptionException {
        return DecryptionUtil.decrypt(keyPair.getPrivate(), activityDTO);
    }

    protected AuthenticationDTO decrypt(final KeyPair keyPair, final AuthenticationDTO authenticationDTO) throws ContentDecryptionException {
        return DecryptionUtil.decrypt(keyPair.getPrivate(), authenticationDTO);
    }

    protected InterestDTO decrypt(final KeyPair keyPair, final InterestDTO interestDTO) throws ContentDecryptionException {
        return DecryptionUtil.decrypt(keyPair.getPrivate(), interestDTO);
    }

    protected ActivationDTO encrypt(final ActivationDTO activationDTO) throws ContentEncryptionException {
        return EncryptionUtil.encrypt(BACKEND_PUBLIC_KEY, activationDTO);
    }

    protected AuthenticationDTO encrypt(final AuthenticationDTO authenticationDTO) throws ContentEncryptionException {
        return EncryptionUtil.encrypt(BACKEND_PUBLIC_KEY, authenticationDTO);
    }

}
