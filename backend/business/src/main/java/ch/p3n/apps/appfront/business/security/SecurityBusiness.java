package ch.p3n.apps.appfront.business.security;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Interface which describes methods to be available by security business.
 *
 * @author deluc1
 * @author zempm3
 */
public interface SecurityBusiness extends ApplicationListener<ContextRefreshedEvent> {

    /**
     * @return private key of this backend application.
     */
    PrivateKey getBackendPrivateKey();

    /**
     * @return public key of this backend application.
     */
    PublicKey getBackendPublicKey();

    /**
     * Method to create a hash from clientId, interestId, client random and server random for matching purposes.
     *
     * @param clientId     client id.
     * @param interestId   interest id.
     * @param clientRandom client random.
     * @return client secret used for hashing.
     */
    String createHash(final String clientId, final String interestId, final String clientRandom);

}
