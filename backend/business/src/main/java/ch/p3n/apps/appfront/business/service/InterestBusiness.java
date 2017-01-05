package ch.p3n.apps.appfront.business.service;

import ch.p3n.apps.appfront.business.exception.InvalidActivityNameException;
import ch.p3n.apps.appfront.business.exception.InvalidClientPushTokenException;
import ch.p3n.apps.appfront.business.exception.InvalidInterestIdException;
import ch.p3n.apps.appfront.business.exception.NoActivationFoundException;
import ch.p3n.apps.appfront.domain.entity.InterestEntity;

import java.util.Collection;

/**
 * Describes the available services offered by the interest business.<br>
 * This is used by rest controller and handles business logic for interests.
 *
 * @author deluc1
 * @author zempm3
 */
public interface InterestBusiness {

    /**
     * Method to create a new interest entity based on activation data.
     *
     * @param clientId           client id.
     * @param clientRandom       client random.
     * @param clientPushToken    client push token.
     * @param visibilityType     visibility type.
     * @param visibilityDuration visibility duration.
     * @param activities         activities.
     * @return interest entity.
     * @throws InvalidActivityNameException    in case an invalid activity name was provided.
     * @throws InvalidClientPushTokenException in case an invalid client push token was provided.
     */
    InterestEntity createInterest(final String clientId, final String clientRandom, final String clientPushToken, final int visibilityType, final int visibilityDuration, final Collection<String> activities) throws InvalidActivityNameException, InvalidClientPushTokenException;

    /**
     * Method to get an existing interest entity based on activation data.
     *
     * @param interestId interest id.
     * @return interest entity.
     * @throws InvalidInterestIdException in case an invalid interest id was provided.
     */
    InterestEntity getInterest(final String interestId) throws InvalidInterestIdException;

    /**
     * Method to verify the interest hash.
     *
     * @param interestEntity interest entity.
     * @param clientId       client id.
     * @param clientRandom   client random.
     * @throws NoActivationFoundException in case that the activation cannot be found.
     */
    void verifyInterestHash(final InterestEntity interestEntity, final String clientId, final String clientRandom) throws NoActivationFoundException;

    /**
     * Method to delete an existing interest entity.
     *
     * @param interestEntity interest entity.
     */
    void deleteInterest(final InterestEntity interestEntity);

}
