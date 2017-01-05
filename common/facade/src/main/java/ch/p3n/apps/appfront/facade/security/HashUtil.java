package ch.p3n.apps.appfront.facade.security;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Util to create hashes of strings.
 *
 * @author deluc1
 * @author zempm3
 */
public class HashUtil {

    private HashUtil() {
        // only a private constructor as all methods are static.
    }

    /**
     * Create hash of a string.
     *
     * @param textToHash text to hash.
     * @return hash of text.
     */
    public static String createHash(final String textToHash) {
        final byte[] textBytes = StringUtils.getBytesUtf8(textToHash);
        try {
            final MessageDigest md = MessageDigest.getInstance(SecuritySpec.HASH_ALGORITHM);
            final byte[] hashBytes = md.digest(textBytes);
            return Hex.encodeHexString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
