package ch.p3n.apps.appfront.rest.controller;

import ch.p3n.apps.appfront.api.dto.ActivationDTO;
import ch.p3n.apps.appfront.business.exception.InvalidClientIdException;
import ch.p3n.apps.appfront.business.exception.InvalidInterestIdException;
import ch.p3n.apps.appfront.business.exception.MissingClientIdException;
import ch.p3n.apps.appfront.business.exception.MissingInterestIdException;
import ch.p3n.apps.appfront.rest.Util;
import org.springframework.util.StringUtils;

/**
 * Abstract rest controller with common functionality.
 *
 * @author deluc1
 * @author zempm3
 */
public class AbstractRestController {

    /**
     * Validate activation data.
     *
     * @param decryptedActivationData decrypted activation data.
     * @throws MissingClientIdException in case of missing client id.
     * @throws InvalidClientIdException in case of invalid client id.
     * @throws MissingInterestIdException in case of missing interest id.
     * @throws InvalidInterestIdException in case of invalid interest id.
     */
    protected void validateActivationData(ActivationDTO decryptedActivationData) throws MissingClientIdException, InvalidClientIdException, MissingInterestIdException, InvalidInterestIdException {
        if (decryptedActivationData.getAuthentication() == null
                || StringUtils.isEmpty(decryptedActivationData.getAuthentication().getClientId())) {
            throw new MissingClientIdException();
        } else if (!Util.isUUID(decryptedActivationData.getAuthentication().getClientId())) {
            throw new InvalidClientIdException();
        } else if (decryptedActivationData.getInterest() == null
                || StringUtils.isEmpty(decryptedActivationData.getInterest().getInterestId())) {
            throw new MissingInterestIdException();
        } else if (!Util.isUUID(decryptedActivationData.getInterest().getInterestId())) {
            throw new InvalidInterestIdException();
        }
    }

}
