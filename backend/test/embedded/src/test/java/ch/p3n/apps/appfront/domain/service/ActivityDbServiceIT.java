package ch.p3n.apps.appfront.domain.service;

import ch.p3n.apps.appfront.domain.entity.ActivityEntity;
import ch.p3n.apps.appfront.test.util.TestUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

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
public class ActivityDbServiceIT {

    @Autowired
    private ActivityDbService activityDbService;

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
