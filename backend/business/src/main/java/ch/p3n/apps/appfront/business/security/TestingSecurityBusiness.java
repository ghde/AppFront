package ch.p3n.apps.appfront.business.security;

import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * Security bootstrap component for test execution.
 *
 * @author deluc1
 * @author zempm3
 */
@Component
@Profile("junit")
public class TestingSecurityBusiness extends AbstractSecurityBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestingSecurityBusiness.class);

    private static final String PRIVATE_KEY_STRING = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCNmiyVBdDXKpMvggdlOLO1m3JN6HRui/GHEPxBR7YAeQ8HzLUyw9FX8feSUj1TbOaQabgJCvL1st1MFyeHUnbG8+086LvgTxUoeD4P9KGLJBGxc75Chtycb7z0vVrW/yGB5puVSaKxTdS9LILhH6tEsWG0UdfSgGm5PHERo16F9AwlL7SJDU8bGcWhTSjjl2jdlNg/7kTJdrkZX8fvdoN2GGN4u0VtNP2ok8NzecfmyJH+CaafThzBKGMhxA0dyPemDwTiBvYxdG0qRJFM8+mcWy+lq2y0zoTTgX06fBkR9fwH1UPWxaYdkNKzjHsspJWFgfVpsFmZ2jJW5QHZ4QoLAgMBAAECggEAWpiLcYTZCvDYXoroxGA0yjp8NVBGPfwXOX0qh3GkBkVt0mWsgKA8LfJHnTw0tE12bmrVLiPtMjmP0ID372JZUAz8ef3FRkwSk2PrATZYrLcVAt20msfCGex7jnIfznJJM90hKbCFAL91Htt9mR8z6q06f63lUW6LNdJv3QMTFMwJ4siF5FRVkOx7yW/Xe2u/SfWJxdXkaH6p+SOXBubUPsG490YnyFUjHbFEwGgARlr8uHyj+vscnzNf0RzHv24Yp9CrGGzhkkRJWbGOqtcvYemSA1Qt/ZMLS9VAQ9T8MMr2RIHohGR6CnVIlUu3e3Q3jZUAbzU0pFTT0et1XL+xcQKBgQDkG8S6/mM1ry6nQduP2oUeqsY+a+/GCWTo9q/9p8XSjuIfqQtTPo6YBqkjCYWI2c1eAW9ZZLg0tx2jTNPtxfoaeqM78ggLsIhpfsktm98beRp3gJNmv46bZfDNMyOy6tLaR0S+lEk+KyPOukVU/p+2MfZJimQWGbNik+RQz/oVFQKBgQCe6phh+cT2uRDuZZ/riUoy6gmkUi7P1ws3YRYwa60ntHCGQaskBhq7oNbwpzkyWWnQe+FOBl2LLImgNwk/P6TPMFJbPa5iuXJcNe9dm7m2Cf64YjU2GDcT/VUGYIaLKk4VQ4ZLOEWKRzKRceHnh/QgC4KyQdqLjzw3WpSCDNaqnwKBgBclYi7/RR3basZ5/kd4iu1zsq3+0dOsfFrPPUhlz8Lv0K4ZvxZxUJLIij0N6EjcoOQbDStq9u4SbqV1VEPaROiO7SVWB8732L+rp8pC+L6W6UKa/1n7sgK+s1J/D+5FuaOAe89CyLPQOM/vQr2/IIGiTDVmH4XGkdc83nv8vomJAoGALSaB/PQ13dNe2BzPfuAW0Lhl3OXsnuh+K1HOOBufqrQ9dCecNDP3zG86Ik2glomI4s8PiFeOpEgXIgoA+pNeg+86tumjbjE6KLC6PWJNNUdJs6FTdPcUTv2e9pzeHRP781aoBR8LwunPmDs+78VUuvYKQBMtwJEFjsSRhRGYvAkCgYEA17C+v68CuN+Y7k8IgroP6oxT35X+9cHbRLjzzoKqUuvY3KKf7U5Y6qTJiAHWnWQDqw7qRsAs1lM/dD65e88q8qcv7kSj+LqnKE6SGNCdIFjzhe7oAh6aQWd6tl0GOEcMsNq454xBGHl8eG8sEpKfvbD0OFC+LYj2I+Nag7q8ITk=";

    public static final String PUBLIC_KEY_STRING = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjZoslQXQ1yqTL4IHZTiztZtyTeh0bovxhxD8QUe2AHkPB8y1MsPRV/H3klI9U2zmkGm4CQry9bLdTBcnh1J2xvPtPOi74E8VKHg+D/ShiyQRsXO+QobcnG+89L1a1v8hgeablUmisU3UvSyC4R+rRLFhtFHX0oBpuTxxEaNehfQMJS+0iQ1PGxnFoU0o45do3ZTYP+5EyXa5GV/H73aDdhhjeLtFbTT9qJPDc3nH5siR/gmmn04cwShjIcQNHcj3pg8E4gb2MXRtKkSRTPPpnFsvpatstM6E04F9OnwZEfX8B9VD1sWmHZDSs4x7LKSVhYH1abBZmdoyVuUB2eEKCwIDAQAB";

    private PrivateKey privateKey;

    private PublicKey publicKey;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        super.onApplicationEvent(event);

        LOGGER.info("Using predefined (bundled) private/public keypair.");
        try {
            privateKey = KeyGeneratorUtil.getPrivateKey(PRIVATE_KEY_STRING);
            publicKey = KeyGeneratorUtil.getPublicKey(PUBLIC_KEY_STRING);
        } catch (InvalidKeySpecException e) {
            LOGGER.error("Existing keys are invalid", e);
        }
    }

    @Override
    public PrivateKey getBackendPrivateKey() {
        return privateKey;
    }

    @Override
    public PublicKey getBackendPublicKey() {
        return publicKey;
    }


}
