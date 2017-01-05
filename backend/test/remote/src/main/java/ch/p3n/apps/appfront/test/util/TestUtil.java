package ch.p3n.apps.appfront.test.util;

import ch.p3n.apps.appfront.api.dto.ActivationDTO;
import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;

/**
 * Test helper utility.
 *
 * @author deluc1
 * @author zempm3
 */
public class TestUtil {

    private TestUtil() {
        // private constructor as class only contains static methods.
    }

    public static String url(final String relativePath) {
        return "http://localhost:7712/appfront-backend-test-remote/rest/" + relativePath;
    }

    public static ActivationDTO createActivationDTO() {
        final ActivationDTO activationDTO = new ActivationDTO();
        activationDTO.setVisibilityDuration(100);
        return activationDTO;
    }

    public static AuthenticationDTO createAuthenticationDTO(final String publicKeyString) {
        final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientPublicKey(publicKeyString);
        return authenticationDTO;
    }

    public static AuthenticationDTO createAuthDtoWithClientId(final String clientId) {
        final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientId(clientId);
        return authenticationDTO;
    }

}
