package ch.p3n.apps.appfront.facade.security;

import ch.p3n.apps.appfront.facade.exception.KeyFileReadException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Key generator utility which generates private and public key.
 *
 * @author deluc1
 * @author zempm3
 */
public class KeyGeneratorUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyGeneratorUtil.class);

    private static final String UNKNOWN_ALGORITHM = "KeyPairGenerator algorithm unknown";

    private static final String PRK = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCU5eop+6khaoV239p/MUNCiJ8QTeFlw/yOaskqudVQ4C7oQlp5V7eLhFHTFuIEvwZYMQPsbg8H/kRYqGiTYj2tltvDhU2i1Twrr4ohvugUc6lwcHOVEPDT1xbtwDBDnlB1z0cnckBi5GE6S5Mbfn9QNI4m/eYBagyBZxR31ykODqohcipsrTAGkY8pNo6SJAVCmqoR5DFZVjxqsNIktM/WQZjK5fUiErfqYy7CVCdRNtEvO8/SvDo9O80UcRaDBmNCXHlJu5d848j9bn8fVn6/JtrcDYv3YkT0UoT7HwCGyQrKFUeYfHJwHvGmTbGt1oLk8rxfhLNE7GyWI7weLG79AgMBAAECggEBAIqj4Xsh5oNEyey71pij5/qld2YY/gxhhmGdMJ5l1iet9j8wuUlO0YpItF+WaPD+ZQvs57hXaguJR149wNGqob9mPk45auZ5IcOTeUyekz2nzFbzfcuIsJSvK5xIXSg42rY3QAuMqELsX1OGVv08tZKavNm1g1pBwk8jCb3kS0fYFLjGardBBfGRrFcH6yZ/m5kdnVQvpIf6CYtcknZe4HJnG662GPUh7Ms6MXjgRRrS2JD1Pn9cJ2X7sZ/c2nrEdTTZkW6nMB03Ka7qpcKG0gBHyOdAs7ld8DdsMBS69ezyG8hAhuMf8pTELnq39aCWTKlQFIUrPy7vL67NSgzBKAECgYEA39rI8XoKWRnxWz4N8RrxCBs8c2jPeF2ZNwROGuIS5F8MWJCTRbt+x4J03fXTeaUXWwgGYDKlgMdvpGi/zdParsi8eemb8KkU2dQPUgXRdBtEbxMXfyK+Wk97ztclwLe4FnggEYTJPCoWQb+RflBnQRxUZ8Rbiu0AB364g2zOtQECgYEAqkefA3GIqjFJQv5DNkMB8k1LxyTvquFy5M5CK5B01audzbMLk+iVbKvuXO9u23RLE9XH0UmJ9It9k2x+b8KQwDl+aNeTTUzIzjIBDtsPGokPqmPTIAcRcD0hqCIQi/PIVQ9lN/DrE54/YQcR6mqZdsMrhEiZnOTbDsVbQmIyjf0CgYAz5CugjoHHldP1KrVgprYpAudIeYteg5P4xO62HKH0fnQGM71v9L620O72ZP3YvQRk/b88nYDwEUs3+XA87ldLHQ1T1WtopklHFXlXsjGTkJ6UgQB5YDxwAuzYTddajTwHOWF1ripX4Am4xAeeGFVXKpIGW+Y8liPjmOR80sQ4AQKBgGgSZB6fqm6StvsqGfw1EiB8m574dT8UkbtEUO79WFsri8Qn9SCOW51Sq9nDQRcByc0ysPC6KIX9J2AORGycFwpVWWxMravmDSjxKPJicqW+NYPnhScjCsZ1pndzcneHJN7QDQYWyiZwZr4vqwSB6I/Mk3FDShe5TCWH+9Hj4JAlAoGBAKP3oRoLJxjwjvw2LvDuBSFiut+PLea9HCD9mHQ2y7FI+a10JPnwh3qqkzKBawhyNP9HGCu7uzd+7fn3DXKyjdY2n0jdPXCp/c4xBAeSOYb267WpI/8R1ZJI3C/2/SU8dz5H9bziYaSk4/IOUv0vJRHxaZE+vuy9lnJJPV45yFis";

    private static final String PBK = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlOXqKfupIWqFdt/afzFDQoifEE3hZcP8jmrJKrnVUOAu6EJaeVe3i4RR0xbiBL8GWDED7G4PB/5EWKhok2I9rZbbw4VNotU8K6+KIb7oFHOpcHBzlRDw09cW7cAwQ55Qdc9HJ3JAYuRhOkuTG35/UDSOJv3mAWoMgWcUd9cpDg6qIXIqbK0wBpGPKTaOkiQFQpqqEeQxWVY8arDSJLTP1kGYyuX1IhK36mMuwlQnUTbRLzvP0rw6PTvNFHEWgwZjQlx5SbuXfOPI/W5/H1Z+vyba3A2L92JE9FKE+x8AhskKyhVHmHxycB7xpk2xrdaC5PK8X4SzROxsliO8Hixu/QIDAQAB";

    private KeyGeneratorUtil() {
        // only a private constructor as all methods are static.
    }

    /**
     * Generates a private and public key (aka keypair) and writes the private key to the {@code privateKeyOutputStream} and the public key to the {@code publicKeyOutputStream}.
     * Caller must close the output streams on his own!
     *
     * @param privateKeyOutputStream private key output stream.
     * @param publicKeyOutputStream  public key output stream.
     */
    public static void generateKeyPair(final OutputStream privateKeyOutputStream, final OutputStream publicKeyOutputStream) {
        try {
            IOUtils.write(PRK, privateKeyOutputStream, Charset.defaultCharset());
            IOUtils.write(PBK, publicKeyOutputStream, Charset.defaultCharset());
        } catch (IOException e) {
            LOGGER.error("Unable to write private/public key", e);
        } finally {
            IOUtils.closeQuietly(privateKeyOutputStream);
            IOUtils.closeQuietly(privateKeyOutputStream);
        }
    }

    /**
     * Generates a private and public key (aka keypair).
     *
     * @return keypair.
     */
    public static KeyPair generateKeyPair() {
        KeyPair keyPair = null;
        try {
            LOGGER.info("Generating private/public keypair using algorithm : " + SecuritySpec.KEY_PAIR_ALGORITHM);
            final PrivateKey privateKey = getPrivateKey(PRK);
            final PublicKey publicKey = getPublicKey(PBK);
            keyPair = new KeyPair(publicKey, privateKey);
        } catch (InvalidKeySpecException e) {
            LOGGER.error(UNKNOWN_ALGORITHM, e);
        }

        return keyPair;
    }

    /**
     * Creates a string representation of a private of public key.
     *
     * @param key key.
     * @return string representation of the given key.
     */
    public static String getKeyAsString(final Key key) {
        return new String(Base64.encodeBase64(key.getEncoded()));
    }

    /**
     * Creates a private key from give string representation (base64 encoded).
     *
     * @param privateKeyString private key as string (base64 encoded).
     * @return private key object.
     * @throws InvalidKeySpecException if the provided key is invalid.
     */
    public static PrivateKey getPrivateKey(final String privateKeyString) throws InvalidKeySpecException {
        PrivateKey privateKey = null;
        try {
            final byte[] decodedPrivateKeyBytes = Base64.decodeBase64(privateKeyString.getBytes());
            final KeyFactory keyFactory = KeyFactory.getInstance(SecuritySpec.KEY_PAIR_ALGORITHM);
            final EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decodedPrivateKeyBytes);
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(UNKNOWN_ALGORITHM, e);
        }

        return privateKey;
    }

    /**
     * Creates a private key from the given file input stream {@code privateKeyFileInputStream}.
     * Caller must close the input stream on his own!
     *
     * @param privateKeyInputStream private key input stream.
     * @return private key object.
     * @throws IOException             if public key cannot be read.
     * @throws InvalidKeySpecException if the provided key is invalid.
     */
    public static PrivateKey getPrivateKey(final InputStream privateKeyInputStream) throws KeyFileReadException {
        PrivateKey privateKey = null;
        try {
            final byte[] privateKeyBytes = IOUtils.toByteArray(privateKeyInputStream);
            final byte[] decodedPrivateKeyBytes = Base64.decodeBase64(privateKeyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance(SecuritySpec.KEY_PAIR_ALGORITHM);
            final EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decodedPrivateKeyBytes);
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            LOGGER.error("Error while reading private key from file", e);
            throw new KeyFileReadException(e);
        }

        return privateKey;
    }

    /**
     * Creates a public key from give string representation (base64 encoded).
     *
     * @param publicKeyString public key as string (base64 encoded).
     * @return public key object.
     * @throws InvalidKeySpecException if the provided key is invalid.
     */
    public static PublicKey getPublicKey(final String publicKeyString) throws InvalidKeySpecException {
        PublicKey publicKey = null;
        try {
            final byte[] decodedPublicKeyBytes = Base64.decodeBase64(publicKeyString.getBytes());
            final KeyFactory keyFactory = KeyFactory.getInstance(SecuritySpec.KEY_PAIR_ALGORITHM);
            final EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decodedPublicKeyBytes);
            publicKey = keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(UNKNOWN_ALGORITHM, e);
        }

        return publicKey;
    }

    /**
     * Creates a public key from the given file input stream {@code publicKeyFileInputStream}.
     * Caller must close the input stream on his own!
     *
     * @param publicKeyInputStream public key input stream.
     * @return public key object.
     * @throws KeyFileReadException if the public key cannot be read.
     */
    public static PublicKey getPublicKey(final InputStream publicKeyInputStream) throws KeyFileReadException {
        PublicKey publicKey = null;
        try {
            final byte[] publicKeyBytes = IOUtils.toByteArray(publicKeyInputStream);
            final byte[] decodedPublicKeyBytes = Base64.decodeBase64(publicKeyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance(SecuritySpec.KEY_PAIR_ALGORITHM);
            final EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decodedPublicKeyBytes);
            publicKey = keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            LOGGER.error("Error while reading public key from file", e);
            throw new KeyFileReadException(e);
        }

        return publicKey;
    }

}
