package ch.p3n.apps.appfront.rest.controller;

import ch.p3n.apps.appfront.api.controller.RegistrationController;
import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.business.exception.InvalidClientPublicKeyException;
import ch.p3n.apps.appfront.business.exception.MissingClientPublicKeyException;
import ch.p3n.apps.appfront.business.service.AuthenticationBusiness;
import ch.p3n.apps.appfront.domain.entity.AuthenticationEntity;
import ch.p3n.apps.appfront.facade.exception.ContentEncryptionException;
import ch.p3n.apps.appfront.facade.security.EncryptionUtil;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * Default implementation of {@link RegistrationController} for rest services.
 *
 * @author deluc1
 * @author zempm3
 */
@RestController
@RequestMapping(value = "/register", produces = "application/json")
public class RegistrationRestController implements RegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationRestController.class);

    @Autowired
    private AuthenticationBusiness authenticationBusiness;

    @Override
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public AuthenticationDTO postRegister(@RequestBody final AuthenticationDTO authenticationData) throws BusinessException {
        try {
            if (StringUtils.isEmpty(authenticationData.getClientPublicKey())) {
                throw new MissingClientPublicKeyException();
            }

            // Read public key in order to ensure it is valid.
            final PublicKey clientPublicKey = KeyGeneratorUtil.getPublicKey(authenticationData.getClientPublicKey());

            // Create new registration.
            final AuthenticationEntity authenticationEntity = authenticationBusiness.createRegistration(authenticationData.getClientPublicKey());

            // Create response object
            final AuthenticationDTO authDto = new AuthenticationDTO();
            authDto.setClientId(authenticationEntity.getClientId());

            // Encrypt the response for the client.
            return EncryptionUtil.encrypt(clientPublicKey, authDto);

        } catch (InvalidKeySpecException | ContentEncryptionException e) {
            LOGGER.warn("Unable to encrypt the selected content", e);
            throw new InvalidClientPublicKeyException();
        }
    }

}
