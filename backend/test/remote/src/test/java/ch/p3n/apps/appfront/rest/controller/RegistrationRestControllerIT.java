package ch.p3n.apps.appfront.rest.controller;

import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.KeyPair;
import java.util.UUID;

/**
 * Integration tests for {@link RegistrationRestController}.
 *
 * @author deluc1
 * @author zempm3
 */
public class RegistrationRestControllerIT extends AbstractRestControllerIT {

    private static final KeyPair KEY_PAIR = KeyGeneratorUtil.generateKeyPair();

    private static final KeyPair KEY_PAIR_2 = KeyGeneratorUtil.generateKeyPair();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testPostRegister_missingClientId() throws Exception {

        // This request will fail as of missing client public key.
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.MISSING_CLIENT_ID.name());

        // Make request
        final AuthenticationDTO resp = postRegister(new AuthenticationDTO());
        Assert.assertNotNull("Response is not null", resp);
        Assert.assertNotNull("Response client id is not null", resp.getClientId());
    }

    @Test
    public void testPostRegister_missingClientPublicKey() throws Exception {

        // This request will fail as of missing client public key.
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.MISSING_CLIENT_PUBLIC_KEY.name());

        // Construct request object
        final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientId(UUID.randomUUID().toString());

        // Make request
        final AuthenticationDTO resp = postRegister(authenticationDTO);
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
        authenticationDTO.setClientId(UUID.randomUUID().toString());
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

    @Test
    public void testPostRegister_withDeviceIdAsClientId() throws Exception {
        final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientId("68749711c72f7e80");
        authenticationDTO.setClientPublicKey(KeyGeneratorUtil.getKeyAsString(KEY_PAIR.getPublic()));
        final AuthenticationDTO createdAuthenticationDTO = postRegisterAndDecrypt(KEY_PAIR, authenticationDTO);
        Assert.assertEquals("ClientId is correct", authenticationDTO.getClientId(), createdAuthenticationDTO.getClientId());
    }

    @Test
    public void testPostRegister_withInvalidClientId() throws Exception {
        final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientId("&D&S*((");
        authenticationDTO.setClientPublicKey(KeyGeneratorUtil.getKeyAsString(KEY_PAIR.getPublic()));

        // This request will fail as of missing client public key.
        exception.expect(BusinessException.class);
        exception.expectMessage(BusinessError.INVALID_CLIENT_ID.name());

        final AuthenticationDTO createdAuthenticationDTO = postRegisterAndDecrypt(KEY_PAIR, authenticationDTO);
        Assert.assertEquals("ClientId is correct", authenticationDTO.getClientId(), createdAuthenticationDTO.getClientId());
    }

    @Test
    public void testPostRegister_registerAgainWithSameClientIdButDifferentKey() throws Exception {
        final String clientId = "d8749711c72f7e80";

        // Authentication DTO for first registration
        final String clientPublicKey = KeyGeneratorUtil.getKeyAsString(KEY_PAIR.getPublic());
        final AuthenticationDTO authDto1 = new AuthenticationDTO();
        authDto1.setClientId(clientId);
        authDto1.setClientPublicKey(clientPublicKey);

        // Authentication DTO for second registration
        final String clientPublicKey2 = KeyGeneratorUtil.getKeyAsString(KEY_PAIR_2.getPublic());
        final AuthenticationDTO authDto2 = new AuthenticationDTO();
        authDto2.setClientId(clientId);
        authDto2.setClientPublicKey(clientPublicKey2);

        // First registration
        final AuthenticationDTO resp1 = postRegisterAndDecrypt(KEY_PAIR, authDto1);
        Assert.assertNotNull("Response is not null", resp1);
        Assert.assertEquals("client id is correct", clientId, resp1.getClientId());

        // Second registration
        final AuthenticationDTO resp2 = postRegisterAndDecrypt(KEY_PAIR_2, authDto2);
        Assert.assertNotNull("Response is not null", resp2);
        Assert.assertEquals("client id is correct", clientId, resp2.getClientId());
    }

}
