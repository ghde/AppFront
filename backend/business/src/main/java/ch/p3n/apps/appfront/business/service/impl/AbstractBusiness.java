package ch.p3n.apps.appfront.business.service.impl;

import ch.p3n.apps.appfront.business.exception.*;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Pattern;

/**
 * Abstract business with helper methods.
 *
 * @author zempm3
 * @author deluc1
 */
public abstract class AbstractBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBusiness.class);

    private static final Pattern PATTERN_CLIENT_ID = Pattern.compile("^([a-zA-Z0-9\\-]+)$");

    private static final Pattern PATTERN_INTEREST_ID = Pattern.compile("^([a-zA-Z0-9\\-]+)$");

    private static final Pattern PATTERN_ACTIVITY_NAME = Pattern.compile("^([\\w\\s\\-]+)$");

    private static final Pattern PATTERN_CLIENT_PUSH_TOKEN = Pattern.compile("^([a-zA-Z0-9]+)$");

    protected void validateClientId(final String clientId) throws InvalidClientIdException {
        if (!PATTERN_CLIENT_ID.matcher(clientId).matches()) {
            throw new InvalidClientIdException();
        }
    }

    protected void validateInterestId(final String interestId) throws InvalidInterestIdException {
        if (!PATTERN_INTEREST_ID.matcher(interestId).matches()) {
            throw new InvalidInterestIdException();
        }
    }

    protected void validateActivityName(final String activityName) throws InvalidActivityNameException {
        if (!PATTERN_ACTIVITY_NAME.matcher(norm(activityName)).matches()) {
            throw new InvalidActivityNameException();
        }
    }

    protected void validateClientPushToken(final String clientPushToken) throws InvalidClientPushTokenException {
        if (!PATTERN_CLIENT_PUSH_TOKEN.matcher(clientPushToken).matches()) {
            throw new InvalidClientPushTokenException();
        }
    }

    protected void validateClientPublicKey(final String clientPublicKeyString) throws InvalidClientPublicKeyException {
        try {
            final PublicKey clientPublicKey = KeyGeneratorUtil.getPublicKey(clientPublicKeyString);
            if (clientPublicKey == null) {
                throw new InvalidClientPublicKeyException();
            }
        } catch (InvalidKeySpecException e) {
            LOGGER.debug("Invalid key specification", e);
            throw new InvalidClientPublicKeyException();
        }
    }

    private String norm(final String text) {
        return text
                .replaceAll("Â|À|Å|Ã", "A")
                .replaceAll("â|à|å|ã", "a")
                .replaceAll("Ä", "AE")
                .replaceAll("ä", "ae")
                .replaceAll("Ç", "C")
                .replaceAll("ç", "c")
                .replaceAll("É|Ê|È|Ë", "E")
                .replaceAll("é|ê|è|ë", "e")
                .replaceAll("Ó|Ô|Ò|Õ|Ø", "O")
                .replaceAll("ó|ô|ò|õ", "o")
                .replaceAll("Ö", "OE")
                .replaceAll("ö", "oe")
                .replaceAll("Š", "S")
                .replaceAll("š", "s")
                .replaceAll("ß", "ss")
                .replaceAll("Ú|Û|Ù", "U")
                .replaceAll("ú|û|ù", "u")
                .replaceAll("Ü", "UE")
                .replaceAll("ü", "ue")
                .replaceAll("Ý|Ÿ", "Y")
                .replaceAll("ý|ÿ", "y")
                .replaceAll("Ž", "Z")
                .replaceAll("ž/", "z");
    }

}
