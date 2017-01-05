package ch.p3n.apps.appfront.business.service.impl;

import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.business.exception.InvalidActivityNameException;
import ch.p3n.apps.appfront.business.service.ActivityBusiness;
import ch.p3n.apps.appfront.domain.entity.ActivityEntity;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.UUID;

/**
 * Integration test class for {@link ActivityBusinessImpl}.
 *
 * @author deluc1
 * @author zempm3
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("junit")
@ContextConfiguration("/embedded-testing-context.xml")
public class ActivityBusinessImplIT {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private ActivityBusiness activityBusiness;

    @Test
    public void testGetActivities() {
        final Collection<ActivityEntity> entities = activityBusiness.getActivities();
        Assert.assertNotNull("entity list not null", entities);
        Assert.assertFalse("entity list not empty", entities.isEmpty());
        entities.stream().forEach(entity -> {
            Assert.assertNotNull("entity id not null", entity.getId());
            Assert.assertNotNull("entity name not null", entity.getName());
            Assert.assertTrue("entity name is not empty", entity.getName().length() > 0);
        });
    }

    @Test
    public void testCreateActivity() throws Exception {
        final String activityName = "testMe" + Long.toString(System.currentTimeMillis());
        createActivityAndCheckName(activityName);
    }

    @Test
    public void testCreateActivity_umlauts() throws Exception {
        final String activityName = "Läuten" + Long.toString(System.currentTimeMillis());
        createActivityAndCheckName(activityName);
    }

    @Test
    public void testCreateActivity_invalidName() throws Exception {
        final String activityName = "testMe-" + Long.toString(System.currentTimeMillis());
        createActivityAndCheckName(activityName);
    }

    @Test
    public void testCreateActivity_html() throws Exception {
        exception.expect(InvalidActivityNameException.class);
        exception.expectMessage(BusinessError.INVALID_ACTIVITY_NAME.name());

        final String activityName = "<script>alert('a');</script>";
        createActivityAndCheckName(activityName);
    }

    private void createActivityAndCheckName(final String activityName) throws InvalidActivityNameException {

        // Create new activity entity.
        final ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setName(activityName);

        // Store activity entity.
        final ActivityEntity createdEntity = activityBusiness.createActivity(activityEntity);

        // Get all stored activities.
        final Collection<ActivityEntity> entities = activityBusiness.getActivities();
        final ActivityEntity readEntity = entities.stream().filter(e -> e.getId() == createdEntity.getId()).findFirst().get();
        Assert.assertNotNull("Entity is really stored in database", readEntity);
        Assert.assertEquals("Entity name is as expected", activityName, readEntity.getName());
    }
}
