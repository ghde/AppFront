package ch.p3n.apps.appfront.rest.controller;

import ch.p3n.apps.appfront.api.controller.NotificationController;
import ch.p3n.apps.appfront.api.dto.ActivationDTO;
import ch.p3n.apps.appfront.api.dto.MatchDTO;
import ch.p3n.apps.appfront.api.dto.MatchType;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.business.exception.InvalidInterestIdException;
import ch.p3n.apps.appfront.business.exception.InvalidRequestEncryptionException;
import ch.p3n.apps.appfront.business.exception.NoActivationFoundException;
import ch.p3n.apps.appfront.business.exception.NoRegistrationFoundException;
import ch.p3n.apps.appfront.business.security.SecurityBusiness;
import ch.p3n.apps.appfront.business.service.AuthenticationBusiness;
import ch.p3n.apps.appfront.business.service.InterestBusiness;
import ch.p3n.apps.appfront.business.service.MatchRequestBusiness;
import ch.p3n.apps.appfront.domain.entity.AuthenticationEntity;
import ch.p3n.apps.appfront.domain.entity.InterestEntity;
import ch.p3n.apps.appfront.facade.exception.ContentDecryptionException;
import ch.p3n.apps.appfront.facade.security.DecryptionUtil;
import ch.p3n.apps.appfront.rest.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Default implementation of {@link NotificationController} for rest services.
 *
 * @author deluc1
 * @author zempm3
 */
@RestController
@RequestMapping(produces = "application/json")
public class NotificationRestController extends AbstractRestController implements NotificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationRestController.class);

    @Autowired
    private SecurityBusiness securityBusiness;

    @Autowired
    private AuthenticationBusiness authenticationBusiness;

    @Autowired
    private InterestBusiness interestBusiness;

    @Autowired
    private MatchRequestBusiness matchRequestBusiness;

    @Override
    @RequestMapping(value = "/notify/{otherClientInterestId}", method = RequestMethod.POST, consumes = "application/json")
    public void postNotify(@RequestBody final ActivationDTO activationData, @PathVariable final String otherClientInterestId) throws BusinessException {

        try {

            // Decrypt & validate activation data
            final ActivationDTO decryptedActivationData = DecryptionUtil.decrypt(securityBusiness.getBackendPrivateKey(), activationData);
            validateActivationData(decryptedActivationData);

            // Validate otherClientInterestId
            if (!Util.isUUID(otherClientInterestId)) {
                throw new InvalidInterestIdException();
            } else if (otherClientInterestId.equals(decryptedActivationData.getInterest().getInterestId())) {
                throw new InvalidInterestIdException();
            }

            // Get existing authentication.
            final AuthenticationEntity authenticationEntity = authenticationBusiness.getRegistration(decryptedActivationData.getAuthentication().getClientId());
            if (authenticationEntity == null) {
                throw new NoRegistrationFoundException();
            }

            // Get interest & validate hash of originator
            final InterestEntity interestEntity = interestBusiness.getInterest(//
                    decryptedActivationData.getInterest().getInterestId());
            if (interestEntity == null) {
                throw new NoActivationFoundException();
            }

            // Get interest entity of other user
            final InterestEntity interestEntityOther = interestBusiness.getInterest(otherClientInterestId);

            // Create match request if interest do match
            if (matchRequestBusiness.hasInterestMatch(interestEntity, interestEntityOther)) {
                matchRequestBusiness.createMatchRequest(interestEntity, interestEntityOther);
            }

        } catch (ContentDecryptionException e) {
            LOGGER.warn("Client request could not be decrypted", e);
            throw new InvalidRequestEncryptionException();
        }
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/notifications", method = RequestMethod.POST, consumes = "application/json")
    public Collection<MatchDTO> postNotifications(@RequestBody final ActivationDTO activationData) throws BusinessException {
        // TODO : implement.
        final Collection<MatchDTO> resp = new ArrayList<>();
        final MatchDTO dto = new MatchDTO();
        dto.setReason(MatchType.BLUETOOTH);
        resp.add(dto);

        return resp;
    }

}
