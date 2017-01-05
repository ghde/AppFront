package ch.p3n.apps.appfront.rest.controller;

import ch.p3n.apps.appfront.api.dto.*;
import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import ch.p3n.apps.appfront.test.util.TestUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

/**
 * Integration tests for {@link RegistrationRestController}.
 *
 * @author deluc1
 * @author zempm3
 */
public class ActivationRestControllerIT extends AbstractRestControllerIT {

    private static final KeyPair KEY_PAIR = KeyGeneratorUtil.generateKeyPair();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testActivateDeactivate() throws Exception {
        final AuthenticationDTO authenticationDtoRegistered = postRegisterAndDecrypt(KEY_PAIR);
        final Collection<ActivityDTO> resp = postLoginAndDecrypt(KEY_PAIR, authenticationDtoRegistered.getClientId());

        final InterestDTO interestDTO = new InterestDTO();
        interestDTO.setInterests(resp);

        final ActivationDTO activationDTO = new ActivationDTO();
        activationDTO.setAuthentication(TestUtil.createAuthDtoWithClientId(authenticationDtoRegistered.getClientId()));
        activationDTO.setClientPushToken("PUTK");
        activationDTO.setClientRandom(UUID.randomUUID().toString());
        activationDTO.setInterest(interestDTO);
        activationDTO.setVisibilityDuration(10);
        activationDTO.setVisibilityType(MatchType.BLUETOOTH);

        final InterestDTO createdInterestDTO = postActivateAndDecrypt(KEY_PAIR, activationDTO);
        activationDTO.setInterest(createdInterestDTO);
        Assert.assertNotNull("Interest created and not null", createdInterestDTO);
        Assert.assertNotNull("Interest Id not null", createdInterestDTO.getInterestId());

        // Deactivate interest
        postDeactivate(activationDTO);
    }

    @Test
    public void testDeactivate_noClientId() throws Exception {
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.MISSING_CLIENT_ID.name());

        postDeactivate(new ActivationDTO());
    }

    @Test
    public void testDeactivate_invalidClientId() throws Exception {
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.INVALID_CLIENT_ID.name());

        final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientId("GUGUS");

        final ActivationDTO activationDTO = new ActivationDTO();
        activationDTO.setAuthentication(authenticationDTO);

        postDeactivate(activationDTO);
    }

    @Test
    public void testDeactivate_missingInterestId() throws Exception {
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.MISSING_INTEREST_ID.name());

        final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientId(UUID.randomUUID().toString());

        final ActivationDTO activationDTO = new ActivationDTO();
        activationDTO.setAuthentication(authenticationDTO);

        postDeactivate(activationDTO);
    }

    @Test
    public void testDeactivate_invalidInterestId() throws Exception {
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.INVALID_INTEREST_ID.name());

        final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientId(UUID.randomUUID().toString());

        final InterestDTO interestDTO = new InterestDTO();
        interestDTO.setInterestId("X1");

        final ActivationDTO activationDTO = new ActivationDTO();
        activationDTO.setAuthentication(authenticationDTO);
        activationDTO.setInterest(interestDTO);

        postDeactivate(activationDTO);
    }

    @Test
    public void testDeactivate_noRegistrationFound() throws Exception {
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.NO_REGISTRATION_FOUND.name());

        final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientId(UUID.randomUUID().toString());

        final InterestDTO interestDTO = new InterestDTO();
        interestDTO.setInterestId(UUID.randomUUID().toString());

        final ActivationDTO activationDTO = new ActivationDTO();
        activationDTO.setAuthentication(authenticationDTO);
        activationDTO.setInterest(interestDTO);

        postDeactivate(activationDTO);
    }

    @Test
    public void testDeactivate_noActivationFound() throws Exception {
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.NO_ACTIVATION_FOUND.name());

        final AuthenticationDTO authenticationDtoRegistered = postRegisterAndDecrypt(KEY_PAIR);
        final Collection<ActivityDTO> resp = postLoginAndDecrypt(KEY_PAIR, authenticationDtoRegistered.getClientId());

        final InterestDTO interestDTO = new InterestDTO();
        interestDTO.setInterestId(UUID.randomUUID().toString());

        final ActivationDTO activationDTO = new ActivationDTO();
        activationDTO.setAuthentication(authenticationDtoRegistered);
        activationDTO.setInterest(interestDTO);

        postDeactivate(activationDTO);
    }

    @Test
    public void testDeactivate_invalidEncryption() throws Exception {
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.INVALID_ENCRYPTION_IN_REQUEST.name());

        final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientId("GUGUS");

        final ActivationDTO activationDTO = new ActivationDTO();
        activationDTO.setAuthentication(authenticationDTO);

        ACTIVATION_FACADE.postDeactivate(activationDTO);
    }

    @Test
    public void testActivateReplay() throws Exception {
        final AuthenticationDTO authenticationDtoRegistered = postRegisterAndDecrypt(KEY_PAIR);
        postLoginAndDecrypt(KEY_PAIR, authenticationDtoRegistered.getClientId());

        // Legitimate activity DTO
        final ActivityDTO legitimateActivity = new ActivityDTO();
        legitimateActivity.setName("legitimateActivity");

        // Legitimate interest DTO
        final InterestDTO interestDTO = new InterestDTO();
        interestDTO.setInterests(Arrays.asList(legitimateActivity));

        // Legitimate activation DTO
        final ActivationDTO activationDTO = new ActivationDTO();
        activationDTO.setAuthentication(TestUtil.createAuthDtoWithClientId(authenticationDtoRegistered.getClientId()));
        activationDTO.setClientPushToken("PUSH_TOKEN");
        activationDTO.setClientRandom(UUID.randomUUID().toString());
        activationDTO.setInterest(interestDTO);
        activationDTO.setVisibilityDuration(10);
        activationDTO.setVisibilityType(MatchType.BLUETOOTH);

        // Legitimate activation request
        final InterestDTO createdLegitimateInterestDTO = postActivateAndDecrypt(KEY_PAIR, activationDTO);
        activationDTO.setInterest(createdLegitimateInterestDTO);
        Assert.assertNotNull("Interest created and not null", createdLegitimateInterestDTO);
        Assert.assertNotNull("Interest Id not null", createdLegitimateInterestDTO.getInterestId());

        // Unwanted activity DTO
        final ActivityDTO unwantedActivity = new ActivityDTO();
        unwantedActivity.setName("unwantedActivity");
        interestDTO.setInterests(Arrays.asList(unwantedActivity));
        activationDTO.setInterest(interestDTO);

        // Unwanted activation request
        postActivate(activationDTO);
    }

}