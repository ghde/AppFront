package ch.p3n.apps.appfront.facade.controller;

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
import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Integration tests for {@link RegistrationControllerFacade}.
 *
 * @author deluc1
 * @author zempm3
 */
public class RegistrationControllerFacadeTest {

    private static final AuthenticationDTO AUTHENTICATION_DTO = new AuthenticationDTO();

    private static final String SERVICE_URL_REGISTER = "https://appfront.p3n.ch/rest/register";

    private static final KeyPair KEY_PAIR = KeyGeneratorUtil.generateKeyPair();

    private static AuthenticationDTO AUTHENTICATION_DTO_ENCRYPTED;

    @InjectMocks
    private RegistrationControllerFacade registrationControllerFacade;

    @Mock
    private RestTemplate restTemplate;

    @BeforeClass
    public static void setUpClass() throws Exception {
        AUTHENTICATION_DTO.setClientId(UUID.randomUUID().toString());
        AUTHENTICATION_DTO_ENCRYPTED = EncryptionUtil.encrypt(KEY_PAIR.getPublic(), AUTHENTICATION_DTO);
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegister() throws Exception {
        when(restTemplate.postForObject(eq(SERVICE_URL_REGISTER), any(AuthenticationDTO.class), same(AuthenticationDTO.class))).thenReturn(AUTHENTICATION_DTO_ENCRYPTED);
        final AuthenticationDTO authenticationData = new AuthenticationDTO();
        authenticationData.setClientPublicKey("DUMMY_PUBLIC_KEY");

        final AuthenticationDTO encryptedAuthenticationData = EncryptionUtil.encryptForBackend(authenticationData);
        final AuthenticationDTO response = registrationControllerFacade.postRegister(encryptedAuthenticationData);
        Assert.assertNotNull("response is not null", response);
        Assert.assertNotEquals("Generated registration id not matches (as encrypted)", AUTHENTICATION_DTO.getClientId(), response.getClientId());

        final AuthenticationDTO decryptedResponse = DecryptionUtil.decrypt(KEY_PAIR.getPrivate(), response);
        Assert.assertEquals("Generated registration id matches", AUTHENTICATION_DTO.getClientId(), decryptedResponse.getClientId());
    }

}
