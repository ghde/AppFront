package ch.p3n.apps.appfront.facade.controller;

import ch.p3n.apps.appfront.api.dto.ActivityDTO;
import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
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

import static org.mockito.Mockito.*;

/**
 * Integration tests for {@link LoginControllerFacade}.
 *
 * @author deluc1
 * @author zempm3
 */
public class LoginControllerFacadeTest {

    private static final ActivityDTO ACTIVITY_DTO = new ActivityDTO();

    private static final ActivityDTO[] ACTIVITY_DTOS_ENCRYPTED = new ActivityDTO[1];

    private static final String SERVICE_URL_LOGIN = "https://appfront.p3n.ch/rest/login";

    private static final KeyPair KEY_PAIR = KeyGeneratorUtil.generateKeyPair();

    private static ActivityDTO ACTIVITY_DTO_ENCRYPTED;

    @InjectMocks
    private LoginControllerFacade loginControllerFacade;

    @Mock
    private RestTemplate restTemplate;

    @BeforeClass
    public static void setUpClass() throws Exception {
        ACTIVITY_DTO.setName("LoginControllerFacadeTest");
        ACTIVITY_DTO_ENCRYPTED = EncryptionUtil.encrypt(KEY_PAIR.getPublic(), ACTIVITY_DTO);
        ACTIVITY_DTOS_ENCRYPTED[0] = ACTIVITY_DTO_ENCRYPTED;
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegister() throws Exception {
        when(restTemplate.postForObject(eq(SERVICE_URL_LOGIN), any(AuthenticationDTO.class), same(ActivityDTO[].class))).thenReturn(ACTIVITY_DTOS_ENCRYPTED);

        final AuthenticationDTO authenticationData = new AuthenticationDTO();
        authenticationData.setClientPublicKey("DUMMY_PUBLIC_KEY");

        // Make backend call (encrypted)
        final AuthenticationDTO encryptedAuthenticationData = EncryptionUtil.encryptForBackend(authenticationData);
        final Collection<ActivityDTO> response = loginControllerFacade.postLogin(encryptedAuthenticationData);
        Assert.assertNotNull("response is not null", response);
        Assert.assertEquals("Response contains one activity", 1, response.size());
        final ActivityDTO encryptedActivityData = response.iterator().next();
        Assert.assertNotEquals("Response activity name not matches (as encrypted)", ACTIVITY_DTO.getName(), encryptedActivityData.getName());

        // Decrypt and check.
        final ActivityDTO decryptedActivityData = DecryptionUtil.decrypt(KEY_PAIR.getPrivate(), encryptedActivityData);
        Assert.assertEquals("Response activity name matches (as decrypted)", ACTIVITY_DTO.getName(), decryptedActivityData.getName());
    }

}
