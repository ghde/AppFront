package ch.p3n.apps.appfront.business.service.impl;

import ch.p3n.apps.appfront.api.dto.MatchType;
import ch.p3n.apps.appfront.business.exception.InvalidActivityNameException;
import ch.p3n.apps.appfront.business.exception.InvalidClientPublicKeyException;
import ch.p3n.apps.appfront.business.exception.InvalidClientPushTokenException;
import ch.p3n.apps.appfront.business.exception.InvalidInterestIdException;
import ch.p3n.apps.appfront.business.service.ActivityBusiness;
import ch.p3n.apps.appfront.business.service.AuthenticationBusiness;
import ch.p3n.apps.appfront.business.service.InterestBusiness;
import ch.p3n.apps.appfront.business.service.MatchRequestBusiness;
import ch.p3n.apps.appfront.domain.entity.AuthenticationEntity;
import ch.p3n.apps.appfront.domain.entity.InterestEntity;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

/**
 * Integration test class for {@link MatchRequestBusiness}.
 *
 * @author deluc1
 * @author zempm3
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("junit")
@ContextConfiguration("/embedded-testing-context.xml")
public class MatchRequestBusinessImplIT {

    private static final KeyPair KEY_PAIR = KeyGeneratorUtil.generateKeyPair();

    @Autowired
    private AuthenticationBusiness authenticationBusiness;

    @Autowired
    private InterestBusiness interestBusiness;

    @Autowired
    private MatchRequestBusiness matchRequestBusiness;

    @Test
    public void testHasInterestMatch_oneMatch() throws Exception {
        final InterestEntity ie1 = storeInterest(Arrays.asList("a", "b", "c"));
        final InterestEntity ie2 = storeInterest(Arrays.asList("c", "d", "e"));
        Assert.assertTrue(matchRequestBusiness.hasInterestMatch(ie1, ie2));
    }

    @Test
    public void testHasInterestMatch_allMatch() throws Exception {
        final InterestEntity ie1 = storeInterest(Arrays.asList("a", "b", "c"));
        final InterestEntity ie2 = storeInterest(Arrays.asList("a", "b", "c"));
        Assert.assertTrue(matchRequestBusiness.hasInterestMatch(ie1, ie2));
    }

    @Test
    public void testHasInterestMatch_noMatch() throws Exception {
        final InterestEntity ie1 = storeInterest(Arrays.asList("a", "b", "c"));
        final InterestEntity ie2 = storeInterest(Arrays.asList("d", "e"));
        Assert.assertFalse(matchRequestBusiness.hasInterestMatch(ie1, ie2));
    }

    private InterestEntity storeInterest(final Collection<String> activityNames) throws InvalidClientPublicKeyException, InvalidInterestIdException, InvalidActivityNameException, InvalidClientPushTokenException {
        final String clientPublicKey = KeyGeneratorUtil.getKeyAsString(KEY_PAIR.getPublic());
        final AuthenticationEntity registration = authenticationBusiness.createRegistration(clientPublicKey);

        // Create interest.
        final String clientId = registration.getClientId();
        final String clientRandom = UUID.randomUUID().toString();
        final String clientPushToken = Long.toString(System.currentTimeMillis());
        final int visibilityType = MatchType.BLUETOOTH.getTypeId();
        final int visibilityDuration = 10;
        final InterestEntity interest = interestBusiness.createInterest(clientId, clientRandom, clientPushToken, visibilityType, visibilityDuration, activityNames);
        Assert.assertNotNull("Interest entity created and not null", interest);
        Assert.assertNotNull("Interest id set", interest.getInterestId());
        Assert.assertTrue("Interest id not empty", interest.getInterestId().length() > 0);

        // Read interest again.
        return interestBusiness.getInterest(interest.getInterestId());
    }

}
