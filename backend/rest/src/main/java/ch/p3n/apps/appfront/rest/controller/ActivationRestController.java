package ch.p3n.apps.appfront.rest.controller;

import ch.p3n.apps.appfront.api.controller.ActivationController;
import ch.p3n.apps.appfront.api.dto.ActivationDTO;
import ch.p3n.apps.appfront.api.dto.ActivityDTO;
import ch.p3n.apps.appfront.api.dto.InterestDTO;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.business.exception.*;
import ch.p3n.apps.appfront.business.security.SecurityBusiness;
import ch.p3n.apps.appfront.business.service.AuthenticationBusiness;
import ch.p3n.apps.appfront.business.service.InterestBusiness;
import ch.p3n.apps.appfront.domain.entity.AuthenticationEntity;
import ch.p3n.apps.appfront.domain.entity.InterestEntity;
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
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link ActivationController} for rest services.
 *
 * @author deluc1
 * @author zempm3
 */
@RestController
@RequestMapping(produces = "application/json")
public class ActivationRestController extends AbstractRestController implements ActivationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivationRestController.class);

    @Autowired
    private SecurityBusiness securityBusiness;

    @Autowired
    private AuthenticationBusiness authenticationBusiness;

    @Autowired
    private InterestBusiness interestBusiness;

    @Override
    @ResponseBody
    @RequestMapping(value = "activate", method = RequestMethod.POST, consumes = "application/json")
    public InterestDTO postActivate(@RequestBody final ActivationDTO activationData) throws BusinessException {

        try {

            // Decrypt activation data
            final ActivationDTO decryptedActivationData = DecryptionUtil.decrypt(securityBusiness.getBackendPrivateKey(), activationData);
            if (StringUtils.isEmpty(decryptedActivationData.getAuthentication().getClientId())) {
                throw new MissingClientIdException();
            } else if (!Util.isUUID(decryptedActivationData.getAuthentication().getClientId())) {
                throw new InvalidClientIdException();
            }

            // Get existing authentication.
            final AuthenticationEntity authenticationEntity = authenticationBusiness.getRegistration(decryptedActivationData.getAuthentication().getClientId());
            if (authenticationEntity == null) {
                throw new NoRegistrationFoundException();
            }

            // Create Interest
            final Collection<String> activities = decryptedActivationData.getInterest().getInterests().stream().map(ActivityDTO::getName).collect(Collectors.toList());
            final InterestEntity interestEntity = interestBusiness.createInterest(decryptedActivationData.getAuthentication().getClientId(), //
                    decryptedActivationData.getClientRandom(), //
                    decryptedActivationData.getClientPushToken(), //
                    decryptedActivationData.getVisibilityType().getTypeId(), //
                    decryptedActivationData.getVisibilityDuration(), //
                    activities
            );

            // Create response object
            final InterestDTO interestDto = new InterestDTO();
            interestDto.setInterestId(interestEntity.getInterestId());

            final PublicKey clientPublicKey = KeyGeneratorUtil.getPublicKey(authenticationEntity.getClientPublicKey());
            return EncryptionUtil.encrypt(clientPublicKey, interestDto);

        } catch (ContentDecryptionException e) {
            LOGGER.warn("Client request could not be decrypted", e);
            throw new InvalidRequestEncryptionException();
        } catch (ContentEncryptionException e) {
            LOGGER.warn("Content could not be encrypted", e);
            throw new InvalidClientPublicKeyException();
        } catch (InvalidKeySpecException e) {
            LOGGER.warn("Client public key is invalid", e);
            throw new InvalidRequestEncryptionException();
        }
    }

    @Override
    @RequestMapping(value = "deactivate", method = RequestMethod.POST, consumes = "application/json")
    public void postDeactivate(@RequestBody final ActivationDTO activationData) throws BusinessException {

        try {

            // Decrypt activation data
            final ActivationDTO decryptedActivationData = DecryptionUtil.decrypt(securityBusiness.getBackendPrivateKey(), activationData);
            validateActivationData(decryptedActivationData);

            // Get existing authentication.
            final AuthenticationEntity authenticationEntity = authenticationBusiness.getRegistration(decryptedActivationData.getAuthentication().getClientId());
            if (authenticationEntity == null) {
                throw new NoRegistrationFoundException();
            }

            // Get interest
            final InterestEntity interestEntity = interestBusiness.getInterest(//
                    decryptedActivationData.getInterest().getInterestId());
            if (interestEntity == null) {
                throw new NoActivationFoundException();
            }

            interestBusiness.verifyInterestHash(interestEntity, //
                    decryptedActivationData.getAuthentication().getClientId(), //
                    decryptedActivationData.getClientRandom());

            // Delete interest
            interestBusiness.deleteInterest(interestEntity);

        } catch (ContentDecryptionException e) {
            LOGGER.warn("Client request could not be decrypted", e);
            throw new InvalidRequestEncryptionException();
        }
    }

}
