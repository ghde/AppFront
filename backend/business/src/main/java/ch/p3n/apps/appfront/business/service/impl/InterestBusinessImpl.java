package ch.p3n.apps.appfront.business.service.impl;

import ch.p3n.apps.appfront.business.exception.InvalidActivityNameException;
import ch.p3n.apps.appfront.business.exception.InvalidClientPushTokenException;
import ch.p3n.apps.appfront.business.exception.InvalidInterestIdException;
import ch.p3n.apps.appfront.business.exception.NoActivationFoundException;
import ch.p3n.apps.appfront.business.security.SecurityBusiness;
import ch.p3n.apps.appfront.business.service.ActivityBusiness;
import ch.p3n.apps.appfront.business.service.AuthenticationBusiness;
import ch.p3n.apps.appfront.business.service.InterestBusiness;
import ch.p3n.apps.appfront.domain.entity.ActivityEntity;
import ch.p3n.apps.appfront.domain.entity.InterestActivityEntity;
import ch.p3n.apps.appfront.domain.entity.InterestEntity;
import ch.p3n.apps.appfront.domain.service.InterestActivityDbService;
import ch.p3n.apps.appfront.domain.service.InterestDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Default implementation of {@link AuthenticationBusiness}.
 *
 * @author deluc1
 * @author zempm3
 */
@Component
public class InterestBusinessImpl extends AbstractBusiness implements InterestBusiness {

    @Autowired
    private SecurityBusiness securityBusiness;

    @Autowired
    private ActivityBusiness activityBusiness;

    @Autowired
    private InterestDbService interestDbService;

    @Autowired
    private InterestActivityDbService interestActivityDbService;

    @Override
    public InterestEntity createInterest(final String clientId, final String clientRandom, final String clientPushToken, final int visibilityType, final int visibilityDuration, final Collection<String> activities) throws InvalidActivityNameException, InvalidClientPushTokenException {
        validateClientPushToken(clientPushToken);

        // Calculate visibility duration/end date.
        final LocalDateTime time = LocalDateTime.now();
        final LocalDateTime endTime = time.plusMinutes(visibilityDuration);

        // Create interest entity
        final InterestEntity interestEntity = new InterestEntity();
        interestEntity.setClientPushToken(clientPushToken);
        interestEntity.setInterestId(UUID.randomUUID().toString());
        interestEntity.setVisibilityType(visibilityType);
        interestEntity.setVisibilityEndDate(endTime);
        interestEntity.setVerifyHash(securityBusiness.createHash(clientId, interestEntity.getInterestId(), clientRandom));

        // Create or get existing activity
        final Collection<ActivityEntity> activityEntities = getOrCreateActivities(activities);

        // Create interest entry in database
        final InterestEntity createdInterestEntity = interestDbService.createInterest(interestEntity);
        activityEntities.stream().forEach(activity -> {
            final InterestActivityEntity iae = new InterestActivityEntity();
            iae.setActivity(activity);
            iae.setInterest(createdInterestEntity);
            interestActivityDbService.createAuthentication(iae);
        });

        return createdInterestEntity;
    }

    @Override
    public InterestEntity getInterest(final String interestId) throws InvalidInterestIdException {
        validateInterestId(interestId);

        return interestDbService.getInterestByInterestId(interestId);
    }

    @Override
    public void verifyInterestHash(final InterestEntity interestEntity, final String clientId, final String clientRandom) throws NoActivationFoundException {
        final String verifyHash = securityBusiness.createHash(clientId, interestEntity.getInterestId(), clientRandom);
        if (!interestEntity.getVerifyHash().equals(verifyHash)) {
            throw new NoActivationFoundException();
        }
    }

    @Override
    public void deleteInterest(final InterestEntity interestEntity) {
        interestActivityDbService.deleteInterestActivitiesByInterestId(interestEntity.getId());
        interestDbService.deleteInterestById(interestEntity.getId());
    }

    /**
     * Gets or creates the activities which the user entered.
     *
     * @param activities activities.
     * @return activity entities.
     * @throws InvalidActivityNameException in case an invalid activity name was provided.
     */
    private Collection<ActivityEntity> getOrCreateActivities(Collection<String> activities) throws InvalidActivityNameException {
        final Collection<ActivityEntity> activityEntities = new ArrayList<>();
        final Collection<ActivityEntity> existingActivities = activityBusiness.getActivities();
        for (final String activity : activities) {
            validateActivityName(activity);

            final Optional<ActivityEntity> optional = existingActivities.stream()
                    .filter(a -> a.getName().equals(activity)).findFirst();
            if (optional.isPresent()) {
                activityEntities.add(optional.get());
            } else {
                final ActivityEntity newActivity = new ActivityEntity();
                newActivity.setName(activity);
                activityEntities.add(activityBusiness.createActivity(newActivity));
            }
        }
        return activityEntities;
    }

}
