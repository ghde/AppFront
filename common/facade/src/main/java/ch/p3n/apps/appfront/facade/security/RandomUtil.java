package ch.p3n.apps.appfront.facade.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Util to generate random strings.
 *
 * @author deluc1
 * @author zempm3
 */
public class RandomUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomUtil.class);

    private RandomUtil() {
        // only a private constructor as all methods are static.
    }

    /**
     * Create secure random.
     *
     * @return secure random.
     */
    public static String generateSecureRandom() {
        String secureRandom = null;
        try {
            final SecureRandom sr = SecureRandom.getInstance(SecuritySpec.RANDOM_ALGORITHM);
            final long randomLong = sr.nextLong();
            secureRandom = Long.toString(randomLong * randomLong);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Unable to encrypt due to invalid algorithm", e);
        }
        return secureRandom;
    }

}
