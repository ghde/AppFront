package ch.p3n.apps.appfront.business.service.impl;

import ch.p3n.apps.appfront.business.exception.InvalidActivityNameException;
import ch.p3n.apps.appfront.business.service.ActivityBusiness;
import ch.p3n.apps.appfront.domain.entity.ActivityEntity;
import ch.p3n.apps.appfront.domain.service.ActivityDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Default implementation of {@link ActivityBusiness}.
 *
 * @author deluc1
 * @author zempm3
 */
@Component
public class ActivityBusinessImpl extends AbstractBusiness implements ActivityBusiness {

    @Autowired
    private ActivityDbService activityDbService;

    @Override
    public ActivityEntity createActivity(ActivityEntity activityEntity) throws InvalidActivityNameException {
        return activityDbService.createActivity(activityEntity);
    }

    @Override
    public Collection<ActivityEntity> getActivities() {
        return activityDbService.getActivities();
    }

}
