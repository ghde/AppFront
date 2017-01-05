package ch.p3n.apps.appfront.domain.service;

import ch.p3n.apps.appfront.api.dto.MatchType;
import ch.p3n.apps.appfront.domain.entity.InterestEntity;
import ch.p3n.apps.appfront.domain.entity.MatchRequestEntity;
import ch.p3n.apps.appfront.test.util.DateUtil;
import ch.p3n.apps.appfront.test.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.Iterator;

/**
 * Integration test class for {@link ActivityDbService}.
 *
 * @author deluc1
 * @author zempm3
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("junit")
@ContextConfiguration("/embedded-testing-context.xml")
public class MatchRequestDbServiceIT {

    @Autowired
    private InterestDbService interestDbService;

    @Autowired
    private MatchRequestDbService matchRequestDbService;

    @Test
    public void testCreateAndReadCompletedMatchRequestEntity() {

        // Create new instances of InterestEntity
        final InterestEntity createdInterestEntity1 = TestUtil.createInterestEntity(MatchType.BLUETOOTH, DateUtil.getDateInFuture());
        final InterestEntity createdInterestEntity2 = TestUtil.createInterestEntity(MatchType.BLUETOOTH, DateUtil.getDateInFuture());

        // Store entity.
        final InterestEntity storedInterestEntity1 = interestDbService.createInterest(createdInterestEntity1);
        final InterestEntity storedInterestEntity2 = interestDbService.createInterest(createdInterestEntity2);

        // Create new instance of MatchRequestEntity
        final MatchRequestEntity createdMatchRequestEntity1 = TestUtil.createMatchRequestEntity(storedInterestEntity1, storedInterestEntity2);
        final MatchRequestEntity createdMatchRequestEntity2 = TestUtil.createMatchRequestEntity(storedInterestEntity2, storedInterestEntity1);

        // Store first entity and check request.
        final MatchRequestEntity storedMatchRequestEntity1 = matchRequestDbService.createMatchRequest(createdMatchRequestEntity1);
        Assert.assertTrue("Match request entity 1 exists", matchRequestDbService.hasMatchRequestByInterestId(createdMatchRequestEntity1));
        Assert.assertFalse("Match request entity 2 not exists", matchRequestDbService.hasMatchRequestByInterestId(createdMatchRequestEntity2));
        final Collection<MatchRequestEntity> completedMatchRequests1 = matchRequestDbService.getCompletedMatchRequests();
        Assert.assertNotNull("Completed match requests are not null", completedMatchRequests1);
        Assert.assertEquals("Completed match requests are empty", 0, completedMatchRequests1.size());

        // Store first entity and check request.
        final MatchRequestEntity storedMatchRequestEntity2 = matchRequestDbService.createMatchRequest(createdMatchRequestEntity2);
        Assert.assertTrue("Match request entity 2 exists", matchRequestDbService.hasMatchRequestByInterestId(createdMatchRequestEntity2));
        final Collection<MatchRequestEntity> completedMatchRequests2 = matchRequestDbService.getCompletedMatchRequests();
        Assert.assertNotNull("Completed match requests are not null", completedMatchRequests2);
        Assert.assertEquals("Completed match requests are empty", 2, completedMatchRequests2.size());

        // Verify entities
        final Iterator<MatchRequestEntity> it = completedMatchRequests2.iterator();
        final MatchRequestEntity completedMatchRequest1 = it.next();
        final MatchRequestEntity completedMatchRequest2 = it.next();
        Assert.assertTrue("Match id's are correct", completedMatchRequest1.getId() == storedMatchRequestEntity1.getId() || completedMatchRequest1.getId() == storedMatchRequestEntity2.getId());
        Assert.assertTrue("Match id's are correct", completedMatchRequest2.getId() == storedMatchRequestEntity1.getId() || completedMatchRequest2.getId() == storedMatchRequestEntity2.getId());

        // Delete match requests
        matchRequestDbService.deleteMatchRequest(storedMatchRequestEntity1);
        matchRequestDbService.deleteMatchRequest(storedMatchRequestEntity2);
        Assert.assertFalse("Match request entity 1 not exists any more", matchRequestDbService.hasMatchRequestByInterestId(createdMatchRequestEntity1));
        Assert.assertFalse("Match request entity 2 not exists any more", matchRequestDbService.hasMatchRequestByInterestId(createdMatchRequestEntity2));
    }

}

