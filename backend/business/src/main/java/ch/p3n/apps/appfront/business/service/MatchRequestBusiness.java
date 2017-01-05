package ch.p3n.apps.appfront.business.service;

import ch.p3n.apps.appfront.business.exception.InvalidInterestIdException;
import ch.p3n.apps.appfront.domain.entity.InterestEntity;

/**
 * Describes the available services offered by the match request business.<br>
 * This is used by rest controller and handles business logic for interests.
 *
 * @author deluc1
 * @author zempm3
 */
public interface MatchRequestBusiness {

    /**
     * Checks if two interest entities have the same activities.
     *
     * @param interestEntity      interest entity of first user.
     * @param interestEntityOther interest entity of second user.
     * @return {@code true} if interest do match. {@code false} if not.
     */
    boolean hasInterestMatch(final InterestEntity interestEntity, final InterestEntity interestEntityOther);

    /**
     * Creates a new match request for a specific user.
     *
     * @param interestEntity      interest entity of first user.
     * @param interestEntityOther interest entity of second user.
     * @throws InvalidInterestIdException in case an invalid interest id was provided.
     */
    void createMatchRequest(final InterestEntity interestEntity, final InterestEntity interestEntityOther) throws InvalidInterestIdException;

}
