package ch.p3n.apps.appfront.business.security;

import ch.p3n.apps.appfront.facade.exception.KeyFileReadException;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Security bootstrap component.
 *
 * @author deluc1
 * @author zempm3
 */
@Component
@Profile("standalone")
public class StandaloneSecurityBusiness extends AbstractSecurityBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(StandaloneSecurityBusiness.class);

    private static final String PRIVATE_KEY = "%s/.ssh/app-front.key";

    private static final String PUBLIC_KEY = "%s/.ssh/app-front.pub";

    private PrivateKey privateKey;

    private PublicKey publicKey;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        super.onApplicationEvent(event);

        final String userHome = System.getProperty("user.home");
        final Path privateKeyPath = Paths.get(String.format(PRIVATE_KEY, userHome));
        final Path publicKeyPath = Paths.get(String.format(PUBLIC_KEY, userHome));
        LOGGER.info("Reading key files from filesystem path.");
        LOGGER.debug(" publicKey -> " + publicKeyPath.toString());
        LOGGER.debug(" privateKey -> " + privateKeyPath.toString());
        if (!Files.exists(privateKeyPath) || !Files.exists(publicKeyPath)) {
            generateKeyPair(privateKeyPath, publicKeyPath);
        } else {
            readExistingKeyPair(privateKeyPath, publicKeyPath);
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

    private void generateKeyPair(final Path privateKeyPath, final Path publicKeyPath) {
        try {

            // Delete existing private key.
            if (Files.exists(privateKeyPath)) {
                Files.delete(privateKeyPath);
            }

            // Delete existing public key.
            if (Files.exists(publicKeyPath)) {
                Files.delete(publicKeyPath);
            }

            // Create new file.
            final Path createdPrivateKeyPath = Files.createFile(privateKeyPath);
            final Path createdPublicKeyPath = Files.createFile(publicKeyPath);

            // Generate KeyPair
            final KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
            Files.write(createdPrivateKeyPath, Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
            Files.write(createdPublicKeyPath, Base64.encodeBase64(keyPair.getPublic().getEncoded()));
        } catch (IOException e) {
            LOGGER.error("Unable to generate key pair", e);
        }
    }

    private void readExistingKeyPair(final Path privateKeyPath, final Path publicKeyPath) {
        InputStream isPrivateKey = null;
        InputStream isPublicKey = null;
        try {

            // Get input streams from key files.
            isPrivateKey = Files.newInputStream(privateKeyPath);
            isPublicKey = Files.newInputStream(publicKeyPath);

            // Read keys
            privateKey = KeyGeneratorUtil.getPrivateKey(isPrivateKey);
            publicKey = KeyGeneratorUtil.getPublicKey(isPublicKey);

        } catch (IOException | KeyFileReadException e) {

            // Close input streams first.
            IOUtils.closeQuietly(isPrivateKey);
            IOUtils.closeQuietly(isPublicKey);

            // Try to generate new keys.
            LOGGER.error("Unable to read existing keys, try to create new ones.", e);
            generateKeyPair(privateKeyPath, publicKeyPath);
        } finally {
            IOUtils.closeQuietly(isPrivateKey);
            IOUtils.closeQuietly(isPublicKey);
        }
    }

}
