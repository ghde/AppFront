package ch.p3n.apps.appfront.facade.security;

import ch.p3n.apps.appfront.api.dto.*;
import ch.p3n.apps.appfront.facade.exception.ContentDecryptionException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Util to decrypt string with given public key.
 *
 * @author deluc1
 * @author zempm3
 */
public class DecryptionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecryptionUtil.class);

    private static final String CIPHER_SUITE = "RSA/ECB/OAEPPadding";

    private DecryptionUtil() {
        // only a private constructor as all methods are static.
    }

    /**
     * Decrypts the givens string with the given private key.
     *
     * @param privateKey      private key to decrypt with.
     * @param stringToDecrypt string to decrypt.
     * @return decrypted string.
     * @throws ContentDecryptionException if the content decryption failed.
     */
    public static String decrypt(final PrivateKey privateKey, final String stringToDecrypt) throws ContentDecryptionException {
        if (StringUtils.isEmpty(stringToDecrypt)) {
            return stringToDecrypt;
        }

        String decryptedString = null;
        try {
            final byte[] bytesToDecrypt = Base64.decodeBase64(stringToDecrypt.getBytes());
            final Cipher cipher = Cipher.getInstance(CIPHER_SUITE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decryptedString = new String(cipher.doFinal(bytesToDecrypt));
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Unable to decrypt due to invalid algorithm", e);
        } catch (NoSuchPaddingException e) {
            LOGGER.error("Unable to decrypt due to unknown padding", e);
        } catch (IllegalBlockSizeException e) {
            LOGGER.error("Unable to decrypt due to illegal block", e);
        } catch (InvalidKeyException e) {
            LOGGER.error("Unable to decrypt due to invalid key", e);
            throw new ContentDecryptionException(e);
        } catch (BadPaddingException e) {
            LOGGER.error("Unable to decrypt due to bad padding of provided data", e);
            throw new ContentDecryptionException(e);
        }

        return decryptedString;
    }

    /**
     * Method to decrypt {@link ActivityDTO} with given {@link PrivateKey}.
     *
     * @param privateKey    private key used for decryption.
     * @param dataToDecrypt data to be decrypted.
     * @return decrypted data.
     * @throws ContentDecryptionException
     */
    public static ActivationDTO decrypt(final PrivateKey privateKey, final ActivationDTO dataToDecrypt) throws ContentDecryptionException {
        final ActivationDTO decryptedData = new ActivationDTO();
        if (dataToDecrypt.getAuthentication() != null) {
            decryptedData.setAuthentication(decrypt(privateKey, dataToDecrypt.getAuthentication()));
        }
        if (!StringUtils.isEmpty(dataToDecrypt.getClientPushToken())) {
            decryptedData.setClientPushToken(decrypt(privateKey, dataToDecrypt.getClientPushToken()));
        }
        if (!StringUtils.isEmpty(dataToDecrypt.getClientPushToken())) {
            decryptedData.setClientRandom(decrypt(privateKey, dataToDecrypt.getClientPushToken()));
        }
        if (dataToDecrypt.getInterest() != null) {
            decryptedData.setInterest(decrypt(privateKey, dataToDecrypt.getInterest()));
        }
        decryptedData.setVisibilityType(dataToDecrypt.getVisibilityType());
        decryptedData.setVisibilityDuration(dataToDecrypt.getVisibilityDuration());
        return decryptedData;
    }

    /**
     * Method to decrypt {@link ActivityDTO} with given {@link PrivateKey}.
     *
     * @param privateKey    private key used for decryption.
     * @param dataToDecrypt data to be decrypted.
     * @return decrypted data.
     * @throws ContentDecryptionException
     */
    public static ActivityDTO decrypt(final PrivateKey privateKey, final ActivityDTO dataToDecrypt) throws ContentDecryptionException {
        final ActivityDTO decryptedData = new ActivityDTO();
        if (!StringUtils.isEmpty(dataToDecrypt.getName())) {
            decryptedData.setName(DecryptionUtil.decrypt(privateKey, dataToDecrypt.getName()));
        }
        return decryptedData;
    }

    /**
     * Method to decrypt {@link AuthenticationDTO} with given {@link PrivateKey}.
     *
     * @param privateKey    private key used for decryption.
     * @param dataToDecrypt data to be decrypted.
     * @return decrypted data.
     * @throws ContentDecryptionException
     */
    public static AuthenticationDTO decrypt(final PrivateKey privateKey, final AuthenticationDTO dataToDecrypt) throws ContentDecryptionException {
        final AuthenticationDTO decryptedData = new AuthenticationDTO();
        if (!StringUtils.isEmpty(dataToDecrypt.getClientId())) {
            decryptedData.setClientId(DecryptionUtil.decrypt(privateKey, dataToDecrypt.getClientId()));
        }
        if (!StringUtils.isEmpty(dataToDecrypt.getClientPublicKey())) {
            decryptedData.setClientPublicKey(dataToDecrypt.getClientPublicKey());
        }
        if (!StringUtils.isEmpty(dataToDecrypt.getClientRandom())) {
            decryptedData.setClientRandom(DecryptionUtil.decrypt(privateKey, dataToDecrypt.getClientRandom()));
        }
        return decryptedData;
    }

    /**
     * Method to decrypt {@link InterestDTO} with given {@link PrivateKey}.
     *
     * @param privateKey    private key used for decryption.
     * @param dataToDecrypt data to be decrypted.
     * @return decrypted data.
     * @throws ContentDecryptionException
     */
    public static InterestDTO decrypt(final PrivateKey privateKey, final InterestDTO dataToDecrypt) throws ContentDecryptionException {
        final InterestDTO decryptedData = new InterestDTO();
        decryptedData.setInterestId(DecryptionUtil.decrypt(privateKey, dataToDecrypt.getInterestId()));
        if (dataToDecrypt.getInterests() != null) {
            final Collection<ActivityDTO> encryptedInterests = new ArrayList<>();
            for (ActivityDTO data : dataToDecrypt.getInterests()) {
                encryptedInterests.add(decrypt(privateKey, data));
            }
            decryptedData.setInterests(encryptedInterests);
        }
        return decryptedData;
    }

    /**
     * Method to decrypt {@link MatchDTO} with given {@link PrivateKey}.
     *
     * @param privateKey    private key used for decryption.
     * @param dataToDecrypt data to be decrypted.
     * @return decrypted data.
     * @throws ContentDecryptionException
     */
    public static MatchDTO decrypt(final PrivateKey privateKey, final MatchDTO dataToDecrypt) throws ContentDecryptionException {
        final MatchDTO decryptedData = new MatchDTO();
        decryptedData.setReason(dataToDecrypt.getReason());
        if (dataToDecrypt.getLatitude() != null) {
            decryptedData.setLatitude(DecryptionUtil.decrypt(privateKey, dataToDecrypt.getLatitude()));
        }
        if (dataToDecrypt.getLongitude() != null) {
            decryptedData.setLongitude(DecryptionUtil.decrypt(privateKey, dataToDecrypt.getLongitude()));
        }
        return decryptedData;
    }

}
