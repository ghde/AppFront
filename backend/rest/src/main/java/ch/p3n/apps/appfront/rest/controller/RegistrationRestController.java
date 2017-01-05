package ch.p3n.apps.appfront.rest.controller;

import ch.p3n.apps.appfront.api.controller.RegistrationController;
import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.business.exception.InvalidClientIdException;
import ch.p3n.apps.appfront.business.exception.InvalidClientPublicKeyException;
import ch.p3n.apps.appfront.business.exception.MissingClientIdException;
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
import java.util.regex.Pattern;

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

    private static final Pattern CLIENT_ID_PTRN = Pattern.compile("^([a-zA-Z0-9\\-]+)$");

    @Autowired
    private AuthenticationBusiness authenticationBusiness;

    @Override
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public AuthenticationDTO postRegister(@RequestBody final AuthenticationDTO authenticationData) throws BusinessException {
        try {
            if (StringUtils.isEmpty(authenticationData.getClientId())) {
                throw new MissingClientIdException();
            } else if (StringUtils.isEmpty(authenticationData.getClientPublicKey())) {
                throw new MissingClientPublicKeyException();
            } else if (!CLIENT_ID_PTRN.matcher(authenticationData.getClientId()).matches()) {
                throw new InvalidClientIdException();
            }

            // Read public key in order to ensure it is valid.
            final PublicKey clientPublicKey = KeyGeneratorUtil.getPublicKey(authenticationData.getClientPublicKey());

            // Create new registration.
            final AuthenticationEntity existingAuthenticationEntity = authenticationBusiness.getRegistration(authenticationData.getClientId());
            if (existingAuthenticationEntity == null) {
                authenticationBusiness.createRegistration( //
                        authenticationData.getClientId(), //
                        authenticationData.getClientPublicKey());
            } else {
                authenticationBusiness.updateRegistration( //
                        authenticationData.getClientId(), //
                        authenticationData.getClientPublicKey());
            }

            // Create response object
            final AuthenticationDTO authDto = new AuthenticationDTO();
            authDto.setClientId(authenticationData.getClientId());

            // Encrypt the response for the client.
            return EncryptionUtil.encrypt(clientPublicKey, authDto);

        } catch (InvalidKeySpecException | ContentEncryptionException e) {
            LOGGER.warn("Unable to encrypt the selected content", e);
            throw new InvalidClientPublicKeyException();
        }
    }

}
