package ch.p3n.apps.appfront.rest.controller;

import ch.p3n.apps.appfront.api.dto.ActivityDTO;
import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.facade.controller.LoginControllerFacade;
import ch.p3n.apps.appfront.facade.security.DecryptionUtil;
import ch.p3n.apps.appfront.facade.security.EncryptionUtil;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import ch.p3n.apps.appfront.test.facade.LoginControllerTestFacade;
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
public class LoginRestControllerIT extends AbstractRestControllerIT {

    private static final KeyPair KEY_PAIR = KeyGeneratorUtil.generateKeyPair();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testPostLogin_missingClientId() throws Exception {

        // This request will fail as of missing client public key.
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.MISSING_CLIENT_ID.name());

        // Make request
        final Collection<ActivityDTO> resp = postLogin(new AuthenticationDTO());
        Assert.assertNotNull("Response is not null", resp);
        Assert.assertTrue("Response activity contains 1 entry", resp.isEmpty());
    }

    @Test
    public void testPostLogin_unencryptedClientId() throws Exception {

        // This request will fail as of missing client public key.
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.INVALID_ENCRYPTION_IN_REQUEST.name());

        // Construct request object
        final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientId(UUID.randomUUID().toString());

        // Make request
        final Collection<ActivityDTO> resp = LOGIN_FACADE.postLogin(authenticationDTO);
        Assert.assertNotNull("Response is not null", resp);
        Assert.assertTrue("Response activity contains 1 entry", resp.isEmpty());
    }

    @Test
    public void testPostLogin_invalidClientId() throws Exception {

        // This request will fail as of missing client public key.
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.INVALID_CLIENT_ID.name());

        // Construct request object
        final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientId("INVALID_CLIENT_ID");

        // Make request
        final Collection<ActivityDTO> resp = postLogin(authenticationDTO);
        Assert.assertNotNull("Response is not null", resp);
        Assert.assertTrue("Response activity contains 1 entry", resp.isEmpty());
    }

    @Test
    public void testPostLogin_validUnregisteredClientId() throws Exception {

        // This request will fail as of missing client public key.
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.NO_REGISTRATION_FOUND.name());

        // Make request
        final Collection<ActivityDTO> resp = postLogin();
        Assert.assertNotNull("Response is not null", resp);
        Assert.assertTrue("Response activity contains 1 entry", resp.isEmpty());
    }

    @Test
    public void testPostLogin() throws Exception {
        final AuthenticationDTO authenticationDtoRegistered = postRegisterAndDecrypt(KEY_PAIR);
        final Collection<ActivityDTO> resp = postLogin(authenticationDtoRegistered.getClientId());
        Assert.assertNotNull("Response is not null", resp);
        Assert.assertTrue("Response activity contains equal or more than 5 entries", resp.size() >= 5);
    }

}
