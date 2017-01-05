package ch.p3n.apps.appfront.facade.controller;

import ch.p3n.apps.appfront.api.dto.*;
import ch.p3n.apps.appfront.facade.security.DecryptionUtil;
import ch.p3n.apps.appfront.facade.security.EncryptionUtil;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.security.KeyPair;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Integration tests for {@link RegistrationControllerFacade}.
 *
 * @author deluc1
 * @author zempm3
 */
public class ActivationControllerFacadeTest {

    private static final InterestDTO INTEREST_DTO = new InterestDTO();

    private static final String SERVICE_URL_ACTIVATE = "https://appfront.p3n.ch/rest/activate";

    private static final String SERVICE_URL_DEACTIVATE = "https://appfront.p3n.ch/rest/deactivate";

    private static final KeyPair KEY_PAIR = KeyGeneratorUtil.generateKeyPair();

    private static InterestDTO INTEREST_DTO_ENCRYPTED;

    @InjectMocks
    private ActivationControllerFacade activationControllerFacade;

    @Mock
    private RestTemplate restTemplate;

    @BeforeClass
    public static void setUpClass() throws Exception {
        INTEREST_DTO.setInterestId(UUID.randomUUID().toString());
        INTEREST_DTO_ENCRYPTED = EncryptionUtil.encrypt(KEY_PAIR.getPublic(), INTEREST_DTO);
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPostActivate() throws Exception {
        when(restTemplate.postForObject(eq(SERVICE_URL_ACTIVATE), any(ActivationDTO.class), same(InterestDTO.class))).thenReturn(INTEREST_DTO_ENCRYPTED);

        // Create authentication data
        final AuthenticationDTO authenticationData = new AuthenticationDTO();
        authenticationData.setClientPublicKey("DUMMY_PUBLIC_KEY");

        // Create
        final ActivityDTO activityData = new ActivityDTO();
        activityData.setName("DUMMY_ACTIVITY");

        // Create interest data
        final InterestDTO interestData = new InterestDTO();
        interestData.setInterests(Collections.singletonList(activityData));
        authenticationData.setClientPublicKey("DUMMY_PUBLIC_KEY");

        // Create activation data
        final ActivationDTO activationData = new ActivationDTO();
        activationData.setAuthentication(authenticationData);
        activationData.setInterest(interestData);
        activationData.setVisibilityType(MatchType.BLUETOOTH);
        activationData.setVisibilityDuration(5);

        // Make backend call (encrypted)
        final ActivationDTO encryptedActivationData = EncryptionUtil.encryptForBackend(activationData);
        final InterestDTO response = activationControllerFacade.postActivate(encryptedActivationData);
        Assert.assertNotNull("response is not null", response);
        Assert.assertNotEquals("Client id is not correct (as encrypted)", INTEREST_DTO.getInterestId(), response.getInterestId());

        // Decrypt backend response
        final InterestDTO decryptedResponse = DecryptionUtil.decrypt(KEY_PAIR.getPrivate(), response);
        Assert.assertNotNull("response is not null", decryptedResponse);
        Assert.assertEquals("Client id is correct (as decrypted)", INTEREST_DTO.getInterestId(), decryptedResponse.getInterestId());
    }

    @Test
    public void testPostDeactivate() throws Exception {
        when(restTemplate.postForObject(eq(SERVICE_URL_DEACTIVATE), any(ActivationDTO.class), same(Object.class))).thenReturn(null);

        // Create authentication data
        final AuthenticationDTO authenticationData = new AuthenticationDTO();
        authenticationData.setClientPublicKey("DUMMY_PUBLIC_KEY");

        // Create
        final ActivityDTO activityData = new ActivityDTO();
        activityData.setName("DUMMY_ACTIVITY");

        // Create interest data
        final InterestDTO interestData = new InterestDTO();
        interestData.setInterests(Collections.singletonList(activityData));
        authenticationData.setClientPublicKey("DUMMY_PUBLIC_KEY");

        // Create activation data
        final ActivationDTO activationData = new ActivationDTO();
        activationData.setAuthentication(authenticationData);
        activationData.setInterest(interestData);
        activationData.setVisibilityType(MatchType.BLUETOOTH);
        activationData.setVisibilityDuration(5);

        activationControllerFacade.postDeactivate(activationData);
    }

}
