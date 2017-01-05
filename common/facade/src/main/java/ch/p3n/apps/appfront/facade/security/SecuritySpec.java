package ch.p3n.apps.appfront.facade.security;

/**
 * Contains constants for security relevant settings.
 *
 * @author deluc1
 * @author zempm3
 */
public final class SecuritySpec {

    public static final String KEY_PAIR_ALGORITHM = "RSA";

    public static final int KEY_LENGTH = 512;

    public static final String CIPHER_SUITE = "RSA/ECB/PKCS1Padding";

    public static final String RANDOM_ALGORITHM = "SHA1PRNG";

    public static final String HASH_ALGORITHM = "MD5";

    private SecuritySpec() {
        // private constructor as no public/protected method available.
    }

}


