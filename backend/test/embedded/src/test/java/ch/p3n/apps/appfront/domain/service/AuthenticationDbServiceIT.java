package ch.p3n.apps.appfront.domain.service;

import ch.p3n.apps.appfront.domain.entity.AuthenticationEntity;
import ch.p3n.apps.appfront.test.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test class for {@link ActivityDbService}.
 *
 * @author deluc1
 * @author zempm3
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("junit")
@ContextConfiguration("/embedded-testing-context.xml")
public class AuthenticationDbServiceIT {

    @Autowired
    private AuthenticationDbService authenticationDbService;

    @Test
    public void testCreateAndReadAuthenticationEntity() {

        // Create new instance of AuthenticationEntity
        final AuthenticationEntity createdEntity = TestUtil.createAuthenticationEntity();
        Assert.assertEquals("Entity id is 0 (as not yet inserted)", 0, createdEntity.getId());

        // Store entity.
        final AuthenticationEntity storedEntity = authenticationDbService.createAuthentication(createdEntity);
        Assert.assertNotNull("Entity is stored", storedEntity);
        Assert.assertNotEquals("Entity is saved, id is set.", 0, storedEntity.getId());

        // Fetch entity by name.
        final AuthenticationEntity fetchedEntity = authenticationDbService.getAuthenticationByClientId(createdEntity.getClientId());
        Assert.assertNotNull("Entity is fetched", fetchedEntity);
        Assert.assertTrue("Entity id matches", storedEntity.getId() == fetchedEntity.getId());
        Assert.assertEquals("Entity client id matches", storedEntity.getClientId(), fetchedEntity.getClientId());
        Assert.assertEquals("Entity public key matches", storedEntity.getClientPublicKey(), fetchedEntity.getClientPublicKey());
    }

}
