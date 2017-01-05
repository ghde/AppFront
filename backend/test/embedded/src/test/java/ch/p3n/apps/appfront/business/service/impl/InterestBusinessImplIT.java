package ch.p3n.apps.appfront.business.service.impl;

import ch.p3n.apps.appfront.api.dto.MatchType;
import ch.p3n.apps.appfront.business.service.ActivityBusiness;
import ch.p3n.apps.appfront.business.service.AuthenticationBusiness;
import ch.p3n.apps.appfront.business.service.InterestBusiness;
import ch.p3n.apps.appfront.domain.entity.InterestEntity;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Integration test class for {@link InterestBusinessImpl}.
 *
 * @author deluc1
 * @author zempm3
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("junit")
@ContextConfiguration("/embedded-testing-context.xml")
public class InterestBusinessImplIT {

    private static final KeyPair KEY_PAIR = KeyGeneratorUtil.generateKeyPair();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private AuthenticationBusiness authenticationBusiness;

    @Autowired
    private ActivityBusiness activityBusiness;

    @Autowired
    private InterestBusiness interestBusiness;

    @Test
    public void testActivateDeactivate() throws Exception {
        final String clientId = UUID.randomUUID().toString();
        final String clientPublicKey = KeyGeneratorUtil.getKeyAsString(KEY_PAIR.getPublic());
        authenticationBusiness.createRegistration(clientId, clientPublicKey);
        activityBusiness.getActivities();

        // Create interest.
        final String clientRandom = UUID.randomUUID().toString();
        final String clientPushToken = Long.toString(System.currentTimeMillis());
        final int visibilityType = MatchType.BLUETOOTH.getTypeId();
        final int visibilityDuration = 10;
        final List<String> activitiesList = Arrays.asList(UUID.randomUUID().toString());
        final InterestEntity interest = interestBusiness.createInterest(clientId, clientRandom, clientPushToken, visibilityType, visibilityDuration, activitiesList);
        Assert.assertNotNull("Interest entity created and not null", interest);
        Assert.assertNotNull("Interest id set", interest.getInterestId());
        Assert.assertTrue("Interest id not empty", interest.getInterestId().length() > 0);

        // Read interest again.
        final InterestEntity readInterest = interestBusiness.getInterest(interest.getInterestId());
        Assert.assertNotNull("Interest entity read and not null", readInterest);
        Assert.assertNotNull("Interest id set", readInterest.getInterestId());
        Assert.assertTrue("Interest id not empty", readInterest.getInterestId().length() > 0);

        // Delete interest
        interestBusiness.deleteInterest(readInterest);
    }
}
