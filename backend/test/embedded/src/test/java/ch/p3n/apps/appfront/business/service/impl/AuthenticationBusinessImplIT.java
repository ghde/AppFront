package ch.p3n.apps.appfront.business.service.impl;

import ch.p3n.apps.appfront.api.exception.BusinessError;
import ch.p3n.apps.appfront.business.exception.InvalidClientIdException;
import ch.p3n.apps.appfront.business.exception.InvalidClientPublicKeyException;
import ch.p3n.apps.appfront.business.service.AuthenticationBusiness;
import ch.p3n.apps.appfront.domain.entity.AuthenticationEntity;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.KeyPair;
import java.util.UUID;

/**
 * Integration test class for {@link InterestBusinessImpl}.
 *
 * @author deluc1
 * @author zempm3
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("junit")
@ContextConfiguration("/embedded-testing-context.xml")
public class AuthenticationBusinessImplIT {

    private static final KeyPair KEY_PAIR = KeyGeneratorUtil.generateKeyPair();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private AuthenticationBusiness authenticationBusiness;

    @Test
    public void testRegister() throws Exception {
        final String clientId = UUID.randomUUID().toString();
        final String publicKeyString = KeyGeneratorUtil.getKeyAsString(KEY_PAIR.getPublic());
        final AuthenticationEntity registration = authenticationBusiness.createRegistration(clientId, publicKeyString);
        final AuthenticationEntity readRegistration = authenticationBusiness.getRegistration(registration.getClientId());
        Assert.assertNotNull("Read registration is not null", readRegistration);
        Assert.assertEquals("Client id matches", registration.getClientId(), readRegistration.getClientId());
    }

    @Test
    public void testRegister_invalidPublicKey() throws Exception {
        exception.expect(InvalidClientPublicKeyException.class);
        exception.expectMessage(BusinessError.INVALID_CLIENT_PUBLIC_KEY.name());

        authenticationBusiness.createRegistration("XYZ", "ABC");
    }

    @Test
    public void testRegister_invalidClientId() throws Exception {
        exception.expect(InvalidClientIdException.class);
        exception.expectMessage(BusinessError.INVALID_CLIENT_ID.name());

        final String clientId = UUID.randomUUID().toString();
        final String publicKeyString = KeyGeneratorUtil.getKeyAsString(KEY_PAIR.getPublic());
        final AuthenticationEntity registration = authenticationBusiness.createRegistration(clientId, publicKeyString);
        authenticationBusiness.getRegistration("x-/d");
    }

}
