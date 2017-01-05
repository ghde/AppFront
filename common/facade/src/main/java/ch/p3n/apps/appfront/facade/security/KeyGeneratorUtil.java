package ch.p3n.apps.appfront.facade.security;

import ch.p3n.apps.appfront.facade.exception.KeyFileReadException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

            final KeyPair keyPair = generateKeyPair();
            if (keyPair != null) {
                final PrivateKey privateKey = keyPair.getPrivate();
                final PublicKey publicKey = keyPair.getPublic();

                final byte[] privateKeyBytes = Base64.encodeBase64(privateKey.getEncoded());
                final byte[] publicKeyBytes = Base64.encodeBase64(publicKey.getEncoded());

                privateKeyOutputStream.write(privateKeyBytes);
                publicKeyOutputStream.write(publicKeyBytes);
            }
        } catch (IOException e) {
            LOGGER.error("Unable to write private/public key", e);
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

            final KeyPairGenerator generator = KeyPairGenerator.getInstance(SecuritySpec.KEY_PAIR_ALGORITHM);
            generator.initialize(SecuritySpec.KEY_LENGTH);
            keyPair = generator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
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
