package ch.p3n.apps.appfront.facade.security;

import ch.p3n.apps.appfront.api.dto.*;
import ch.p3n.apps.appfront.facade.exception.ContentDecryptionException;
import ch.p3n.apps.appfront.facade.exception.ContentEncryptionException;
import ch.p3n.apps.appfront.facade.exception.KeyFileReadException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Util to encrypt string with given public key.
 *
 * @author deluc1
 * @author zempm3
 */
public class EncryptionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionUtil.class);

    private static PublicKey backendPublicKey;

    private EncryptionUtil() {
        // private constructor as only static methods.
    }

    private static void initBackendPublicKey() {
        if (backendPublicKey == null) {
            InputStream is = null;
            try {
                is = EncryptionUtil.class.getResourceAsStream("/backend.pub");
                backendPublicKey = KeyGeneratorUtil.getPublicKey(is);
            } catch (KeyFileReadException e) {
                LOGGER.error("Unable to read backend public key", e);
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
    }

    /**
     * Encrypts the givens string with the public key of backend.
     *
     * @param stringToEncrypt string to encrypt.
     * @return encrypted string.
     * @throws ContentEncryptionException in case the content cannot be encrypted.
     */
    public static String encryptForBackend(final String stringToEncrypt) throws ContentEncryptionException {
        initBackendPublicKey();
        return encrypt(backendPublicKey, stringToEncrypt);
    }

    /**
     * Encrypts the givens string with the given public key.
     *
     * @param publicKey       public key to encrypt with.
     * @param stringToEncrypt string to encrypt.
     * @return encrypted string.
     * @throws ContentEncryptionException in case the content encryption failed.
     */
    public static String encrypt(final PublicKey publicKey, final String stringToEncrypt) throws ContentEncryptionException {
        if (StringUtils.isEmpty(stringToEncrypt)) {
            return stringToEncrypt;
        }

        String encryptedString = null;
        try {
            final Cipher cipher = Cipher.getInstance(SecuritySpec.CIPHER_SUITE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            final byte[] encryptedBytes = cipher.doFinal(stringToEncrypt.getBytes());
            encryptedString = new String(Base64.encodeBase64(encryptedBytes));
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Unable to encrypt due to invalid algorithm", e);
        } catch (NoSuchPaddingException e) {
            LOGGER.error("Unable to encrypt due to unknown padding", e);
        } catch (BadPaddingException e) {
            LOGGER.error("Unable to encrypt due to bad padding", e);
        } catch (IllegalBlockSizeException e) {
            LOGGER.error("Unable to encrypt due to illegal block size", e);
        } catch (InvalidKeyException e) {
            LOGGER.error("Unable to encrypt due to invalid key", e);
            throw new ContentEncryptionException(e);
        }

        return encryptedString;
    }

    /**
     * Method to encrypt {@link ActivationDTO} with given {@link PublicKey}.
     *
     * @param publicKey     public key used for encryption.
     * @param dataToEncrypt data to be encrypted.
     * @return encrypted data.
     * @throws ContentDecryptionException
     */
    public static ActivationDTO encrypt(final PublicKey publicKey, final ActivationDTO dataToEncrypt) throws ContentEncryptionException {
        final ActivationDTO encryptedData = new ActivationDTO();
        if (dataToEncrypt.getAuthentication() != null) {
            encryptedData.setAuthentication(encrypt(publicKey, dataToEncrypt.getAuthentication()));
        }
        if (!StringUtils.isEmpty(dataToEncrypt.getClientPushToken())) {
            encryptedData.setClientPushToken(encrypt(publicKey, dataToEncrypt.getClientPushToken()));
        }
        if (!StringUtils.isEmpty(dataToEncrypt.getClientPushToken())) {
            encryptedData.setClientRandom(encrypt(publicKey, dataToEncrypt.getClientPushToken()));
        }
        if (dataToEncrypt.getInterest() != null) {
            encryptedData.setInterest(encrypt(publicKey, dataToEncrypt.getInterest()));
        }
        if (dataToEncrypt.getVisibilityType() != null) {
            encryptedData.setVisibilityType(dataToEncrypt.getVisibilityType());
        }
        encryptedData.setVisibilityDuration(dataToEncrypt.getVisibilityDuration());
        return encryptedData;
    }

    /**
     * Method to encrypt {@link ActivationDTO} with backend public key.
     *
     * @param dataToEncrypt data to be encrypted.
     * @return encrypted data.
     * @throws ContentEncryptionException
     */
    public static ActivationDTO encryptForBackend(final ActivationDTO dataToEncrypt) throws ContentEncryptionException {
        final ActivationDTO encryptedData = new ActivationDTO();
        encryptedData.setAuthentication(encryptForBackend(dataToEncrypt.getAuthentication()));
        if (!StringUtils.isEmpty(dataToEncrypt.getClientPushToken())) {
            encryptedData.setClientPushToken(encryptForBackend(dataToEncrypt.getClientPushToken()));
        }
        if (!StringUtils.isEmpty(dataToEncrypt.getClientPushToken())) {
            encryptedData.setClientRandom(encryptForBackend(dataToEncrypt.getClientPushToken()));
        }
        encryptedData.setInterest(encryptForBackend(dataToEncrypt.getInterest()));
        if (dataToEncrypt.getVisibilityType() != null) {
            encryptedData.setVisibilityType(dataToEncrypt.getVisibilityType());
        }
        encryptedData.setVisibilityDuration(dataToEncrypt.getVisibilityDuration());
        return encryptedData;
    }

    /**
     * Method to encrypt {@link ActivityDTO} with given {@link PublicKey}.
     *
     * @param publicKey     public key used for encryption.
     * @param dataToEncrypt data to be encrypted.
     * @return encrypted data.
     * @throws ContentDecryptionException
     */
    public static ActivityDTO encrypt(final PublicKey publicKey, final ActivityDTO dataToEncrypt) throws ContentEncryptionException {
        final ActivityDTO encryptedData = new ActivityDTO();
        if (!StringUtils.isEmpty(dataToEncrypt.getName())) {
            encryptedData.setName(EncryptionUtil.encrypt(publicKey, dataToEncrypt.getName()));
        }
        return encryptedData;
    }

    /**
     * Method to encrypt {@link ActivityDTO} with backend public key.
     *
     * @param dataToEncrypt data to be encrypted.
     * @return encrypted data.
     * @throws ContentEncryptionException
     */
    public static ActivityDTO encryptForBackend(final ActivityDTO dataToEncrypt) throws ContentEncryptionException {
        final ActivityDTO encryptedData = new ActivityDTO();
        if (!StringUtils.isEmpty(dataToEncrypt.getName())) {
            encryptedData.setName(EncryptionUtil.encryptForBackend(dataToEncrypt.getName()));
        }
        return encryptedData;
    }

    /**
     * Method to encrypt {@link AuthenticationDTO} with given {@link PublicKey}.
     *
     * @param publicKey     public key used for encryption.
     * @param dataToEncrypt data to be encrypted.
     * @return encrypted data.
     * @throws ContentDecryptionException
     */
    public static AuthenticationDTO encrypt(final PublicKey publicKey, final AuthenticationDTO dataToEncrypt) throws ContentEncryptionException {
        final AuthenticationDTO encryptedData = new AuthenticationDTO();
        if (!StringUtils.isEmpty(dataToEncrypt.getClientId())) {
            encryptedData.setClientId(EncryptionUtil.encrypt(publicKey, dataToEncrypt.getClientId()));
        }
        if (!StringUtils.isEmpty(dataToEncrypt.getClientPublicKey())) {
            encryptedData.setClientPublicKey(dataToEncrypt.getClientPublicKey());
        }
        if (!StringUtils.isEmpty(dataToEncrypt.getClientRandom())) {
            encryptedData.setClientRandom(EncryptionUtil.encrypt(publicKey, dataToEncrypt.getClientRandom()));
        }
        return encryptedData;
    }

    /**
     * Method to encrypt {@link AuthenticationDTO} with backend public key.
     *
     * @param dataToEncrypt data to be encrypted.
     * @return encrypted data.
     * @throws ContentEncryptionException
     */
    public static AuthenticationDTO encryptForBackend(final AuthenticationDTO dataToEncrypt) throws ContentEncryptionException {
        final AuthenticationDTO encryptedData = new AuthenticationDTO();
        if (!StringUtils.isEmpty(dataToEncrypt.getClientId())) {
            encryptedData.setClientId(EncryptionUtil.encryptForBackend(dataToEncrypt.getClientId()));
        }
        if (!StringUtils.isEmpty(dataToEncrypt.getClientPublicKey())) {
            encryptedData.setClientPublicKey(dataToEncrypt.getClientPublicKey());
        }
        if (!StringUtils.isEmpty(dataToEncrypt.getClientRandom())) {
            encryptedData.setClientRandom(EncryptionUtil.encryptForBackend(dataToEncrypt.getClientRandom()));
        }
        return encryptedData;
    }

    /**
     * Method to encrypt {@link InterestDTO} with given {@link PublicKey}.
     *
     * @param publicKey     public key used for encryption.
     * @param dataToEncrypt data to be encrypted.
     * @return encrypted data.
     * @throws ContentDecryptionException
     */
    public static InterestDTO encrypt(final PublicKey publicKey, final InterestDTO dataToEncrypt) throws ContentEncryptionException {
        final InterestDTO encryptedData = new InterestDTO();
        encryptedData.setInterestId(EncryptionUtil.encrypt(publicKey, dataToEncrypt.getInterestId()));
        if (dataToEncrypt.getInterests() != null) {
            final Collection<ActivityDTO> encryptedInterests = new ArrayList<>();
            for (ActivityDTO data : dataToEncrypt.getInterests()) {
                encryptedInterests.add(encrypt(publicKey, data));
            }
            encryptedData.setInterests(encryptedInterests);
        }
        return encryptedData;
    }

    /**
     * Method to encrypt {@link InterestDTO} with backend public key.
     *
     * @param dataToEncrypt data to be encrypted.
     * @return encrypted data.
     * @throws ContentEncryptionException
     */
    public static InterestDTO encryptForBackend(final InterestDTO dataToEncrypt) throws ContentEncryptionException {
        final InterestDTO encryptedData = new InterestDTO();
        encryptedData.setInterestId(EncryptionUtil.encryptForBackend(dataToEncrypt.getInterestId()));
        if (dataToEncrypt.getInterests() != null) {
            final Collection<ActivityDTO> encryptedInterests = new ArrayList<>();
            for (ActivityDTO data : dataToEncrypt.getInterests()) {
                encryptedInterests.add(encryptForBackend(data));
            }
            encryptedData.setInterests(encryptedInterests);
        }
        return encryptedData;
    }

    /**
     * Method to encrypt {@link MatchDTO} with given {@link PublicKey}.
     *
     * @param publicKey     public key used for encryption.
     * @param dataToEncrypt data to be encrypted.
     * @return encrypted data.
     * @throws ContentDecryptionException
     */
    public static MatchDTO encrypt(final PublicKey publicKey, final MatchDTO dataToEncrypt) throws ContentEncryptionException {
        final MatchDTO encryptedData = new MatchDTO();
        if (dataToEncrypt.getReason() != null) {
            encryptedData.setReason(dataToEncrypt.getReason());
        }
        if (dataToEncrypt.getLatitude() != null) {
            encryptedData.setLatitude(EncryptionUtil.encrypt(publicKey, dataToEncrypt.getLatitude()));
        }
        if (dataToEncrypt.getLongitude() != null) {
            encryptedData.setLongitude(EncryptionUtil.encrypt(publicKey, dataToEncrypt.getLongitude()));
        }
        return encryptedData;
    }

    /**
     * Method to encrypt {@link MatchDTO} with backend public key.
     *
     * @param dataToEncrypt data to be encrypted.
     * @return encrypted data.
     * @throws ContentEncryptionException
     */
    public static MatchDTO encryptForBackend(final MatchDTO dataToEncrypt) throws ContentEncryptionException {
        final MatchDTO encryptedData = new MatchDTO();
        if (dataToEncrypt.getReason() != null) {
            encryptedData.setReason(dataToEncrypt.getReason());
        }
        if (dataToEncrypt.getLatitude() != null) {
            encryptedData.setLatitude(EncryptionUtil.encryptForBackend(dataToEncrypt.getLatitude()));
        }
        if (dataToEncrypt.getLongitude() != null) {
            encryptedData.setLongitude(EncryptionUtil.encryptForBackend(dataToEncrypt.getLongitude()));
        }
        return encryptedData;
    }

}
