package ch.p3n.apps.appfront.domain.service;

import ch.p3n.apps.appfront.api.dto.MatchType;
import ch.p3n.apps.appfront.domain.entity.InterestEntity;
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

/**
 * Integration test class for {@link ActivityDbService}.
 *
 * @author deluc1
 * @author zempm3
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("junit")
@ContextConfiguration("/embedded-testing-context.xml")
public class InterestDbServiceIT {

    @Autowired
    private InterestDbService interestDbService;

    @Test
    public void testCreateAndReadInterestEntity() {

        // Create new instance of InterestEntity
        final InterestEntity createdEntity = TestUtil.createInterestEntity(MatchType.BLUETOOTH, DateUtil.getDateInFuture());
        Assert.assertEquals("Entity id is 0 (as not yet inserted)", 0, createdEntity.getId());

        // Store entity.
        final InterestEntity storedEntity = interestDbService.createInterest(createdEntity);
        Assert.assertNotNull("Entity is stored", storedEntity);
        Assert.assertNotEquals("Entity is saved, id is set.", 0, storedEntity.getId());

        // Fetch entity by name.
        final InterestEntity fetchedEntity = interestDbService.getInterestByInterestId(createdEntity.getInterestId());
        Assert.assertNotNull("Entity is fetched", fetchedEntity);
        checkInterest(fetchedEntity, storedEntity);
    }

    @Test
    public void testCreateAndDeleteExpired() {

        // Create new instances of InterestEntity
        final InterestEntity notExpiredEntity = TestUtil.createInterestEntity(MatchType.BLUETOOTH, DateUtil.getDateInFuture());
        final InterestEntity expiredEntity = TestUtil.createInterestEntity(MatchType.BLUETOOTH, DateUtil.getDateInPast());

        // Store entities.
        final InterestEntity storedNotExpiredEntity = interestDbService.createInterest(notExpiredEntity);
        interestDbService.createInterest(expiredEntity);

        // Delete expired entities.
        interestDbService.deleteInterestAlreadyExpired();

        // Verify expiration.
        final InterestEntity readNotExpiredEntity = interestDbService.getInterestByInterestId(notExpiredEntity.getInterestId());
        final InterestEntity readExpiredEntity = interestDbService.getInterestByInterestId(expiredEntity.getInterestId());
        Assert.assertNull("Expired entity is not existing any more", readExpiredEntity);
        Assert.assertNotNull("Not expired entity is existing", readNotExpiredEntity);
        checkInterest(readNotExpiredEntity, storedNotExpiredEntity);
    }

    @Test
    public void testCreateAndCheckBroadcast() {

        // Create new instances of InterestEntity
        final InterestEntity notExpiredEntity = TestUtil.createInterestEntity(MatchType.MAP, DateUtil.getDateInFuture());
        final InterestEntity expiredEntity = TestUtil.createInterestEntity(MatchType.MAP, DateUtil.getDateInPast());

        // Store entities.
        final InterestEntity storedNotExpiredEntity = interestDbService.createInterest(notExpiredEntity);
        interestDbService.createInterest(expiredEntity);

        // Delete expired entities.
        final Collection<InterestEntity> interests = interestDbService.getInterestsWithActiveBroadcast();
        Assert.assertNotNull("Interest with active broadcast are existing", interests);
        Assert.assertEquals("One active broadcast interest", 1, interests.size());
        final InterestEntity readNotExpiredEntity = interests.iterator().next();
        Assert.assertNotNull("Not expired entity is existing", readNotExpiredEntity);
        checkInterest(readNotExpiredEntity, storedNotExpiredEntity);
    }

    private void checkInterest(final InterestEntity fetchedEntity, final InterestEntity storedEntity) {
        Assert.assertTrue("Entity id matches", storedEntity.getId() == fetchedEntity.getId());
        Assert.assertEquals("Entity client id matches", storedEntity.getInterestId(), fetchedEntity.getInterestId());
        Assert.assertEquals("Entity client id matches", storedEntity.getClientPushToken(), fetchedEntity.getClientPushToken());
        Assert.assertEquals("Entity client id matches", storedEntity.getVerifyHash(), fetchedEntity.getVerifyHash());
        Assert.assertEquals("Entity client id matches", storedEntity.getVisibilityType(), fetchedEntity.getVisibilityType());
        Assert.assertEquals("Entity public key matches", storedEntity.getVisibilityEndDate(), fetchedEntity.getVisibilityEndDate());
    }

}

