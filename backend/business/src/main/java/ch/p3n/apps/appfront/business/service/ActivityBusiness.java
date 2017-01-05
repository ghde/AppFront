package ch.p3n.apps.appfront.business.service;

import ch.p3n.apps.appfront.business.exception.InvalidActivityNameException;
import ch.p3n.apps.appfront.domain.entity.ActivityEntity;

import java.util.Collection;

/**
 * Describes the available services offered by the activity business.<br>
 * This is used by rest controller and handles business logic for activity.
 *
 * @author deluc1
 * @author zempm3
 */
public interface ActivityBusiness {

    /**
     * Method to create a new activity.
     *
     * @param activityEntity activity to be created.
     * @return activity entity.
     * @throws InvalidActivityNameException in case the activity name is not allowed.
     */
    ActivityEntity createActivity(final ActivityEntity activityEntity) throws InvalidActivityNameException;

    /**
     * Method to fetch activities.
     *
     * @return authentication entity.
     */
    Collection<ActivityEntity> getActivities();

}
