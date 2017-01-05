package ch.p3n.apps.appfront.domain.service;

import ch.p3n.apps.appfront.api.dto.MatchType;
import ch.p3n.apps.appfront.domain.entity.ActivityEntity;
import ch.p3n.apps.appfront.domain.entity.InterestEntity;
import ch.p3n.apps.appfront.test.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.UUID;

/**
 * Integration test class for {@link ActivityDbService}.
 *
 * @author deluc1
 * @author zempm3
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("junit")
@ContextConfiguration("/embedded-testing-context.xml")
public class ActivityDbServiceIT {

    @Autowired
    private ActivityDbService activityDbService;

    @Autowired
    private InterestDbService interestDbService;

    @Test
    public void testCreateActivity_sqlInjection_updateVisibilityOfUsers() {

        // Create sample interest entity and store in database
        final InterestEntity interestEntity = TestUtil.createDummyInterestEntity();
        interestDbService.createInterest(interestEntity);

        // Read interest entity from database to verify if data were changed
        final InterestEntity readInterestEntity1 = interestDbService.getInterestByInterestId(interestEntity.getInterestId());
        Assert.assertEquals("Visibility of interest entity is changed", MatchType.BLUETOOTH.getTypeId(), readInterestEntity1.getVisibilityType());

        // Create an activity with a name that contains text which causes an sql injection
        final String injectedActivityName = UUID.randomUUID() + "'); UPDATE af_interest SET visibility_type = 1; --";
        final ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setName(injectedActivityName);

        // SQL Query which is executed: 'INSERT INTO af_activity (name) VALUES ('${name}')'
        activityDbService.createActivity(activityEntity);

        // Read interest entity from database to verify if data were changed
        final InterestEntity readInterestEntity2 = interestDbService.getInterestByInterestId(interestEntity.getInterestId());
        Assert.assertEquals("Visibility of interest entity is changed", MatchType.MAP.getTypeId(), readInterestEntity2.getVisibilityType());
    }

    @Test
    public void testCreateAndReadActivityEntity() {

        // Create new instance of ActivityEntity
        final ActivityEntity createdEntity = TestUtil.createActivityEntity();
        Assert.assertEquals("Entity id is 0 (as not yet inserted)", 0, createdEntity.getId());

        // Store entity.
        final ActivityEntity storedEntity = activityDbService.createActivity(createdEntity);
        Assert.assertNotNull("Entity is stored", storedEntity);
        Assert.assertNotEquals("Entity is saved, id is set.", 0, storedEntity.getId());

        // Fetch entity by name.
        final ActivityEntity fetchedEntity = activityDbService.getActivityByName(createdEntity.getName());
        Assert.assertNotNull("Entity is fetched", fetchedEntity);
        Assert.assertTrue("Entity id matches", storedEntity.getId() == fetchedEntity.getId());
        Assert.assertEquals("Entity name matches", storedEntity.getName(), fetchedEntity.getName());
    }

    @Test
    public void testCreateAndReadActivities() {

        // Create and store three entities.
        activityDbService.createActivity(TestUtil.createActivityEntity());
        activityDbService.createActivity(TestUtil.createActivityEntity());
        activityDbService.createActivity(TestUtil.createActivityEntity());

        // Fetch entity by name.
        final Collection<ActivityEntity> fetchedEntities = activityDbService.getActivities();
        Assert.assertNotNull("Entities are fetched", fetchedEntities);
        Assert.assertTrue("At least 3 entities exist", fetchedEntities.size() >= 3);
        fetchedEntities.stream().forEach(fetchedEntity -> {
            Assert.assertNotEquals("Entity has an id", 0, fetchedEntity.getName());
            Assert.assertFalse("Entity has a name", StringUtils.isEmpty(fetchedEntity.getName()));
        });
    }

    @Test
    public void testSelectActivityByName() {
        final ActivityEntity createdActivity = activityDbService.createActivity(TestUtil.createActivityEntity());
        final ActivityEntity readActivity = activityDbService.getActivityByName(createdActivity.getName());
        Assert.assertNotNull("Activity exists an can be read", readActivity);
        Assert.assertEquals("Activity id matches", createdActivity.getId(), readActivity.getId());
        Assert.assertEquals("Activity name matches", createdActivity.getName(), readActivity.getName());
    }

}
