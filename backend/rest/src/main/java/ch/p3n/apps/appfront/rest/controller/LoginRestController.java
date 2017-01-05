package ch.p3n.apps.appfront.rest.controller;

import ch.p3n.apps.appfront.api.controller.LoginController;
import ch.p3n.apps.appfront.api.dto.ActivityDTO;
import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.business.exception.*;
import ch.p3n.apps.appfront.business.security.SecurityBusiness;
import ch.p3n.apps.appfront.business.service.ActivityBusiness;
import ch.p3n.apps.appfront.business.service.AuthenticationBusiness;
import ch.p3n.apps.appfront.domain.entity.ActivityEntity;
import ch.p3n.apps.appfront.domain.entity.AuthenticationEntity;
import ch.p3n.apps.appfront.facade.exception.ContentDecryptionException;
import ch.p3n.apps.appfront.facade.exception.ContentEncryptionException;
import ch.p3n.apps.appfront.facade.security.DecryptionUtil;
import ch.p3n.apps.appfront.facade.security.EncryptionUtil;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import ch.p3n.apps.appfront.rest.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Default implementation of {@link LoginController} for rest services.
 *
 * @author deluc1
 * @author zempm3
 */
@RestController
@RequestMapping(value = "login", produces = "application/json")
public class LoginRestController implements LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginRestController.class);

    @Autowired
    private SecurityBusiness securityBusiness;

    @Autowired
    private AuthenticationBusiness authenticationBusiness;

    @Autowired
    private ActivityBusiness activityBusiness;

    @Override
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public Collection<ActivityDTO> postLogin(@RequestBody final AuthenticationDTO authenticationData) throws BusinessException {
        try {
            final AuthenticationDTO decryptedAuthenticationData = DecryptionUtil.decrypt(securityBusiness.getBackendPrivateKey(), authenticationData);
            if (StringUtils.isEmpty(decryptedAuthenticationData.getClientId())) {
                throw new MissingClientIdException();
            } else if (!Util.isUUID(decryptedAuthenticationData.getClientId())) {
                throw new InvalidClientIdException();
            }

            // Get existing authentication.
            final AuthenticationEntity authenticationEntity = authenticationBusiness.getRegistration(decryptedAuthenticationData.getClientId());
            if (authenticationEntity == null) {
                throw new NoRegistrationFoundException();
            }

            final PublicKey clientPublicKey = KeyGeneratorUtil.getPublicKey(authenticationEntity.getClientPublicKey());

            // Get current activities.
            final Collection<ActivityDTO> encryptedActivities = new ArrayList<>();
            for (ActivityEntity activity : activityBusiness.getActivities()) {
                final ActivityDTO activityDto = new ActivityDTO();
                activityDto.setName(activity.getName());

                encryptedActivities.add(EncryptionUtil.encrypt(clientPublicKey, activityDto));
            }

            return encryptedActivities;

        } catch (InvalidKeySpecException e) {
            LOGGER.warn("Client public key is invalid", e);
            throw new InvalidRequestEncryptionException();
        } catch (ContentDecryptionException e) {
            LOGGER.warn("Client request could not be decrypted", e);
            throw new InvalidRequestEncryptionException();
        } catch (ContentEncryptionException e) {
            LOGGER.warn("Content could not be encrypted", e);
            throw new InvalidClientPublicKeyException();
        }
    }

}
