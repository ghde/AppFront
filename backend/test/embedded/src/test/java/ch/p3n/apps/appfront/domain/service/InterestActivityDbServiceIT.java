package ch.p3n.apps.appfront.domain.service;

import ch.p3n.apps.appfront.domain.entity.ActivityEntity;
import ch.p3n.apps.appfront.domain.entity.InterestActivityEntity;
import ch.p3n.apps.appfront.domain.entity.InterestEntity;
import ch.p3n.apps.appfront.test.util.TestUtil;
import org.apache.ibatis.exceptions.PersistenceException;
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
public class InterestActivityDbServiceIT {

    @Autowired
    private InterestActivityDbService interestActivityDbService;

    @Test(expected = PersistenceException.class)
    public void testCreateAndReadInterestActivityEntity() {

        // Create new instance of AuthenticationEntity
        final ActivityEntity ae = TestUtil.createActivityEntity(-1);
        final InterestEntity ie = TestUtil.createInterestEntity(-1);
        final InterestActivityEntity createdEntity = TestUtil.createInterestActivityEntity(ae, ie);

        // Store entity.
        final InterestActivityEntity storedEntity = interestActivityDbService.createAuthentication(createdEntity);
        Assert.assertNotNull("Entity is stored", storedEntity);

        // Fetch entity by activity id.
        final Collection<InterestActivityEntity> fetchedEntitiesByActivityId = interestActivityDbService.getInterestActivitiesByActivityId(ae.getId());
        Assert.assertNotNull("Entities are fetched", fetchedEntitiesByActivityId);
        Assert.assertEquals("Entities list contains one entry", 1, fetchedEntitiesByActivityId.size());
        final InterestActivityEntity fetchedEntityByActivityId = fetchedEntitiesByActivityId.iterator().next();
        checkInterestActivityEntity(fetchedEntityByActivityId, ae, ie);

        // Fetch entity by interest id.
        final Collection<InterestActivityEntity> fetchedEntitiesByInterestId = interestActivityDbService.getInterestActivitiesByInterestId(ie.getId());
        Assert.assertNotNull("Entities are fetched", fetchedEntitiesByInterestId);
        Assert.assertEquals("Entities list contains one entry", 1, fetchedEntitiesByInterestId.size());
        final InterestActivityEntity fetchedEntityByInterestId = fetchedEntitiesByInterestId.iterator().next();
        checkInterestActivityEntity(fetchedEntityByInterestId, ae, ie);
    }

    private void checkInterestActivityEntity(final InterestActivityEntity iae, final ActivityEntity ae, final InterestEntity ie) {
        Assert.assertNotNull("Entity is fetched", iae);
        Assert.assertTrue("Entity activity id is correct", ae.getId() == iae.getActivity().getId());
        Assert.assertTrue("Entity activity id is correct", ie.getId() == iae.getInterest().getId());
        Assert.assertEquals("Entity activity id is correct", ie.getInterestId(), iae.getInterest().getInterestId());
    }

}