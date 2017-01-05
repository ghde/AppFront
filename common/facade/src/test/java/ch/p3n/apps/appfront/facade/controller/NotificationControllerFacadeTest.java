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
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Integration tests for {@link NotificationControllerFacade}.
 *
 * @author deluc1
 * @author zempm3
 */
public class NotificationControllerFacadeTest {

    private static final MatchDTO MATCH_DTO = new MatchDTO();

    private static final MatchDTO[] MATCH_DTOS_ENCRYPTED = new MatchDTO[1];

    private static final String OTHER_CLIENT_INTEREST_ID = UUID.randomUUID().toString();

    private static final String SERVICE_URL_NOTIFY = "https://appfront.p3n.ch/rest/notify/" + OTHER_CLIENT_INTEREST_ID;

    private static final String SERVICE_URL_NOTIFICATIONS = "https://appfront.p3n.ch/rest/notifications";

    private static final KeyPair KEY_PAIR = KeyGeneratorUtil.generateKeyPair();

    private static MatchDTO MATCH_DTO_ENCRYPTED;

    @InjectMocks
    private NotificationControllerFacade notificationControllerFacade;

    @Mock
    private RestTemplate restTemplate;

    @BeforeClass
    public static void setUpClass() throws Exception {
        MATCH_DTO.setLongitude("1525314577");
        MATCH_DTO_ENCRYPTED = EncryptionUtil.encrypt(KEY_PAIR.getPublic(), MATCH_DTO);
        MATCH_DTOS_ENCRYPTED[0] = MATCH_DTO_ENCRYPTED;
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPostNotify() throws Exception {
        when(restTemplate.postForObject(eq(SERVICE_URL_NOTIFY), any(ActivationDTO.class), same(Object.class))).thenReturn(null);

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

        // Post notify
        final ActivationDTO encryptedActivationData = EncryptionUtil.encryptForBackend(activationData);
        notificationControllerFacade.postNotify(encryptedActivationData, OTHER_CLIENT_INTEREST_ID);
    }

    @Test
    public void testPostNotifications() throws Exception {
        when(restTemplate.postForObject(eq(SERVICE_URL_NOTIFICATIONS), any(ActivationDTO.class), same(MatchDTO[].class))).thenReturn(MATCH_DTOS_ENCRYPTED);

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

        // Backend call
        final ActivationDTO encryptedActivationData = EncryptionUtil.encryptForBackend(activationData);
        final Collection<MatchDTO> response = notificationControllerFacade.postNotifications(encryptedActivationData);
        Assert.assertNotNull("response is not null", response);
        Assert.assertEquals("Response contains one match", 1, response.size());
        Assert.assertEquals("Response match type matches", MATCH_DTO.getReason(), response.iterator().next().getReason());
        final MatchDTO encryptedMatchData = response.iterator().next();
        Assert.assertNotEquals("Response longitude not matches (as encrypted)", MATCH_DTO.getLongitude(), encryptedMatchData.getLongitude());

        // Decrypt and check.
        final MatchDTO decryptedMatchData = DecryptionUtil.decrypt(KEY_PAIR.getPrivate(), encryptedMatchData);
        Assert.assertEquals("Response longitude matches (as decrypted)", MATCH_DTO.getLongitude(), decryptedMatchData.getLongitude());
    }

}
