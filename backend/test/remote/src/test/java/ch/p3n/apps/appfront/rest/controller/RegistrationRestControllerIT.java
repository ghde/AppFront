package ch.p3n.apps.appfront.rest.controller;

import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.business.exception.MissingClientPublicKeyException;
import ch.p3n.apps.appfront.facade.controller.RegistrationControllerFacade;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import ch.p3n.apps.appfront.test.facade.RegistrationControllerTestFacade;
import ch.p3n.apps.appfront.test.util.TestUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.web.client.RestTemplate;

import java.security.KeyPair;

/**
 * Integration tests for {@link RegistrationRestController}.
 *
 * @author deluc1
 * @author zempm3
 */
public class RegistrationRestControllerIT extends AbstractRestControllerIT {

    private static final KeyPair KEY_PAIR = KeyGeneratorUtil.generateKeyPair();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testPostRegister_missingClientPublicKey() throws Exception {

        // This request will fail as of missing client public key.
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.MISSING_CLIENT_PUBLIC_KEY.name());

        // Make request
        final AuthenticationDTO resp = postRegister(new AuthenticationDTO());
        Assert.assertNotNull("Response is not null", resp);
        Assert.assertNotNull("Response client id is not null", resp.getClientId());
    }

    @Test
    public void testPostRegister_invalidClientPublicKey() throws Exception {

        // This request will fail as of missing client public key.
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.INVALID_CLIENT_PUBLIC_KEY.name());

        // Construct request object
        final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientPublicKey("INVALID_KEY");

        // Make request
        final AuthenticationDTO resp = postRegister(authenticationDTO);
        Assert.assertNotNull("Response is not null", resp);
        Assert.assertNotNull("Response client id is not null", resp.getClientId());
    }

    @Test
    public void testPostRegister() throws Exception {
        final AuthenticationDTO resp = postRegister(KEY_PAIR);
        Assert.assertNotNull("Response is not null", resp);
        Assert.assertNotNull("Response client id is not null", resp.getClientId());
    }

}
