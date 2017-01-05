package ch.p3n.apps.appfront.business.security;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * Integration test class for {@link SecurityBusiness}.
 *
 * @author deluc1
 * @author zempm3
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("junit")
@ContextConfiguration("/embedded-testing-context.xml")
public class SecurityBusinessIT {

    @Autowired
    private SecurityBusiness securityBusiness;

    @Test
    public void testCreateHash() throws Exception {
        final String clientId = UUID.randomUUID().toString();
        final String interestId = UUID.randomUUID().toString();
        final String clientRandom = UUID.randomUUID().toString();

        final String hash1 = securityBusiness.createHash(clientId, interestId, clientRandom);
        final String hash2 = securityBusiness.createHash(clientId, interestId, clientRandom);
        Assert.assertEquals("Hashes are equal", hash1, hash2);
    }

}
