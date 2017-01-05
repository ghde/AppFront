package ch.p3n.apps.appfront.business.service.impl;

import ch.p3n.apps.appfront.business.exception.InvalidInterestIdException;
import ch.p3n.apps.appfront.business.service.MatchRequestBusiness;
import ch.p3n.apps.appfront.domain.entity.InterestActivityEntity;
import ch.p3n.apps.appfront.domain.entity.InterestEntity;
import ch.p3n.apps.appfront.domain.entity.MatchRequestEntity;
import ch.p3n.apps.appfront.domain.service.InterestActivityDbService;
import ch.p3n.apps.appfront.domain.service.InterestDbService;
import ch.p3n.apps.appfront.domain.service.MatchRequestDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Default implementation of {@link MatchRequestBusiness}.
 *
 * @author deluc1
 * @author zempm3
 */
@Component
public class MatchRequestBusinessImpl extends AbstractBusiness implements MatchRequestBusiness {

    @Autowired
    private InterestDbService interestDbService;

    @Autowired
    private InterestActivityDbService interestActivityDbService;

    @Autowired
    private MatchRequestDbService matchRequestDbService;

    @Override
    public boolean hasInterestMatch(final InterestEntity interestEntity, final InterestEntity interestEntityOther) {
        final Collection<InterestActivityEntity> activities = interestActivityDbService.getInterestActivitiesByInterestId(interestEntity.getId());
        final Collection<InterestActivityEntity> activitiesOther = interestActivityDbService.getInterestActivitiesByInterestId(interestEntityOther.getId());
        return activitiesOther.stream().filter(a ->
                activities.stream().filter(b -> b.getActivity().getId() == a.getActivity().getId()).findFirst().isPresent()
        ).findFirst().isPresent();
    }

    @Override
    public void createMatchRequest(final InterestEntity interestEntity, final InterestEntity interestEntityOther) throws InvalidInterestIdException {
        validateInterestId(interestEntity.getInterestId());
        validateInterestId(interestEntityOther.getInterestId());

        final MatchRequestEntity matchRequestEntity = new MatchRequestEntity();
        matchRequestEntity.setInterest(interestEntity);
        matchRequestEntity.setOtherInterest(interestEntityOther);
        if (!matchRequestDbService.hasMatchRequestByInterestId(matchRequestEntity)) {
            matchRequestDbService.createMatchRequest(matchRequestEntity);
        }
    }

}
