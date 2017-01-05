package ch.p3n.apps.appfront.rest.controller;

import ch.p3n.apps.appfront.api.dto.*;
import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.facade.exception.ContentDecryptionException;
import ch.p3n.apps.appfront.facade.exception.ContentEncryptionException;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import ch.p3n.apps.appfront.test.util.TestUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.KeyPair;
import java.util.Collection;
import java.util.UUID;

/**
 * Integration tests for {@link RegistrationRestController}.
 *
 * @author deluc1
 * @author zempm3
 */
public class NotificationRestControllerIT extends AbstractRestControllerIT {

    private static final KeyPair KEY_PAIR_1 = KeyGeneratorUtil.generateKeyPair();

    private static final KeyPair KEY_PAIR_2 = KeyGeneratorUtil.generateKeyPair();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testPostNotify() throws Exception {
        final ActivationDTO activation1 = activate(KEY_PAIR_1);
        final ActivationDTO activation2 = activate(KEY_PAIR_2);
        postNotify(activation2, activation1.getInterest().getInterestId());
        postNotify(activation1, activation2.getInterest().getInterestId());
    }

    @Test
    public void testPostNotify_interestIdMatchesOtherInterestId() throws Exception {
        final ActivationDTO activation1 = activate(KEY_PAIR_1);

        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.INVALID_INTEREST_ID.name());

        postNotify(activation1, activation1.getInterest().getInterestId());
    }

    @Test
    public void testPostNotify_missingOriginatorCheck() throws Exception {
        final ActivationDTO activation1 = activate(KEY_PAIR_1);
        final ActivationDTO activation2 = activate(KEY_PAIR_2);
        final String interestId1 = activation1.getInterest().getInterestId();
        final String interestId2 = activation2.getInterest().getInterestId();
        postNotify(activation1, interestId2);

        // Exchange interest id in activation
        activation1.getInterest().setInterestId(interestId2);
        postNotify(activation1, interestId1);
    }

    @Test
    public void testPostNotifications() throws Exception {
        final Collection<MatchDTO> matches = postNotifications(TestUtil.createActivationDTO());
        Assert.assertNotNull("Response is not null", matches);
        Assert.assertEquals("Response match reason is BLUETOOTH", MatchType.BLUETOOTH, matches.iterator().next().getReason());
    }

    private ActivationDTO activate(final KeyPair keyPair) throws BusinessException, ContentDecryptionException, ContentEncryptionException {
        final AuthenticationDTO authenticationDtoRegistered = postRegisterAndDecrypt(keyPair);
        final Collection<ActivityDTO> resp = postLoginAndDecrypt(keyPair, authenticationDtoRegistered.getClientId());

        final InterestDTO interestDTO = new InterestDTO();
        interestDTO.setInterests(resp);

        final ActivationDTO activationDTO = new ActivationDTO();
        activationDTO.setAuthentication(TestUtil.createAuthDtoWithClientId(authenticationDtoRegistered.getClientId()));
        activationDTO.setClientPushToken("PSTK");
        activationDTO.setClientRandom(UUID.randomUUID().toString());
        activationDTO.setInterest(interestDTO);
        activationDTO.setVisibilityDuration(10);
        activationDTO.setVisibilityType(MatchType.BLUETOOTH);
        activationDTO.setInterest(postActivateAndDecrypt(keyPair, activationDTO));

        return activationDTO;
    }

}