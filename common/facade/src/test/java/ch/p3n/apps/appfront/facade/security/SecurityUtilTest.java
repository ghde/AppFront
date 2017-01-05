package ch.p3n.apps.appfront.facade.security;

import ch.p3n.apps.appfront.api.dto.*;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collections;
import java.util.UUID;

/**
 * Test for {@link RandomUtil}, {@link EncryptionUtil}, {@link DecryptionUtil} and {@link KeyGeneratorUtil}.
 *
 * @author deluc1
 * @author zempm3
 */
public class SecurityUtilTest {

    @Test
    public void testCreateHash() {
        final String textToHash = "testCreateHash";
        final String hash = HashUtil.createHash(textToHash);
        Assert.assertNotNull(hash);
        Assert.assertNotEquals(textToHash, hash);
    }

    @Test
    public void testGenerateSecureRandom() {
        final String secRandom1 = RandomUtil.generateSecureRandom();
        final String secRandom2 = RandomUtil.generateSecureRandom();
        Assert.assertNotEquals(secRandom1, secRandom2);
    }

    @Test
    public void testEncryptForBackend() throws Exception {
        final String text = "testCreateAndReadKeyPair";
        final String encryptedText = EncryptionUtil.encryptForBackend(text);
        Assert.assertNotNull("Encrypted text is not null", encryptedText);
    }

    @Test
    public void testWithGivenKeys() throws Exception {
        InputStream isPrivateKey = null;
        InputStream isPublicKey = null;
        try {

            // Read private key files.
            isPrivateKey = getClass().getResourceAsStream("/generated-private-key.key");
            isPublicKey = getClass().getResourceAsStream("/generated-public-key.pub");

            // Generate key from file input stream.
            final PrivateKey readPrivateKey = KeyGeneratorUtil.getPrivateKey(isPrivateKey);
            final PublicKey readPublicKey = KeyGeneratorUtil.getPublicKey(isPublicKey);

            // Encrypt & decrypt test.
            final String text = "testWithGivenKeys";
            final String encryptedText = EncryptionUtil.encrypt(readPublicKey, text);
            final String decryptedText = DecryptionUtil.decrypt(readPrivateKey, encryptedText);
            Assert.assertEquals("Decrypted string is correct", text, decryptedText);
        } finally {
            IOUtils.closeQuietly(isPrivateKey);
            IOUtils.closeQuietly(isPublicKey);
        }
    }

    @Test
    public void testWithGivenKeysAsString() throws Exception {
        final String privateKeyString = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCNmiyVBdDXKpMvggdlOLO1m3JN6HRui/GHEPxBR7YAeQ8HzLUyw9FX8feSUj1TbOaQabgJCvL1st1MFyeHUnbG8+086LvgTxUoeD4P9KGLJBGxc75Chtycb7z0vVrW/yGB5puVSaKxTdS9LILhH6tEsWG0UdfSgGm5PHERo16F9AwlL7SJDU8bGcWhTSjjl2jdlNg/7kTJdrkZX8fvdoN2GGN4u0VtNP2ok8NzecfmyJH+CaafThzBKGMhxA0dyPemDwTiBvYxdG0qRJFM8+mcWy+lq2y0zoTTgX06fBkR9fwH1UPWxaYdkNKzjHsspJWFgfVpsFmZ2jJW5QHZ4QoLAgMBAAECggEAWpiLcYTZCvDYXoroxGA0yjp8NVBGPfwXOX0qh3GkBkVt0mWsgKA8LfJHnTw0tE12bmrVLiPtMjmP0ID372JZUAz8ef3FRkwSk2PrATZYrLcVAt20msfCGex7jnIfznJJM90hKbCFAL91Htt9mR8z6q06f63lUW6LNdJv3QMTFMwJ4siF5FRVkOx7yW/Xe2u/SfWJxdXkaH6p+SOXBubUPsG490YnyFUjHbFEwGgARlr8uHyj+vscnzNf0RzHv24Yp9CrGGzhkkRJWbGOqtcvYemSA1Qt/ZMLS9VAQ9T8MMr2RIHohGR6CnVIlUu3e3Q3jZUAbzU0pFTT0et1XL+xcQKBgQDkG8S6/mM1ry6nQduP2oUeqsY+a+/GCWTo9q/9p8XSjuIfqQtTPo6YBqkjCYWI2c1eAW9ZZLg0tx2jTNPtxfoaeqM78ggLsIhpfsktm98beRp3gJNmv46bZfDNMyOy6tLaR0S+lEk+KyPOukVU/p+2MfZJimQWGbNik+RQz/oVFQKBgQCe6phh+cT2uRDuZZ/riUoy6gmkUi7P1ws3YRYwa60ntHCGQaskBhq7oNbwpzkyWWnQe+FOBl2LLImgNwk/P6TPMFJbPa5iuXJcNe9dm7m2Cf64YjU2GDcT/VUGYIaLKk4VQ4ZLOEWKRzKRceHnh/QgC4KyQdqLjzw3WpSCDNaqnwKBgBclYi7/RR3basZ5/kd4iu1zsq3+0dOsfFrPPUhlz8Lv0K4ZvxZxUJLIij0N6EjcoOQbDStq9u4SbqV1VEPaROiO7SVWB8732L+rp8pC+L6W6UKa/1n7sgK+s1J/D+5FuaOAe89CyLPQOM/vQr2/IIGiTDVmH4XGkdc83nv8vomJAoGALSaB/PQ13dNe2BzPfuAW0Lhl3OXsnuh+K1HOOBufqrQ9dCecNDP3zG86Ik2glomI4s8PiFeOpEgXIgoA+pNeg+86tumjbjE6KLC6PWJNNUdJs6FTdPcUTv2e9pzeHRP781aoBR8LwunPmDs+78VUuvYKQBMtwJEFjsSRhRGYvAkCgYEA17C+v68CuN+Y7k8IgroP6oxT35X+9cHbRLjzzoKqUuvY3KKf7U5Y6qTJiAHWnWQDqw7qRsAs1lM/dD65e88q8qcv7kSj+LqnKE6SGNCdIFjzhe7oAh6aQWd6tl0GOEcMsNq454xBGHl8eG8sEpKfvbD0OFC+LYj2I+Nag7q8ITk=";
        final String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjZoslQXQ1yqTL4IHZTiztZtyTeh0bovxhxD8QUe2AHkPB8y1MsPRV/H3klI9U2zmkGm4CQry9bLdTBcnh1J2xvPtPOi74E8VKHg+D/ShiyQRsXO+QobcnG+89L1a1v8hgeablUmisU3UvSyC4R+rRLFhtFHX0oBpuTxxEaNehfQMJS+0iQ1PGxnFoU0o45do3ZTYP+5EyXa5GV/H73aDdhhjeLtFbTT9qJPDc3nH5siR/gmmn04cwShjIcQNHcj3pg8E4gb2MXRtKkSRTPPpnFsvpatstM6E04F9OnwZEfX8B9VD1sWmHZDSs4x7LKSVhYH1abBZmdoyVuUB2eEKCwIDAQAB";

        // Create keys from string.
        final PrivateKey readPrivateKey = KeyGeneratorUtil.getPrivateKey(privateKeyString);
        final PublicKey readPublicKey = KeyGeneratorUtil.getPublicKey(publicKeyString);

        // Create string from keys.
        Assert.assertEquals("private key to string is correct", privateKeyString, KeyGeneratorUtil.getKeyAsString(readPrivateKey));
        Assert.assertEquals("public key to string is correct", publicKeyString, KeyGeneratorUtil.getKeyAsString(readPublicKey));

        // Encrypt & decrypt test.
        final String text = "testWithGivenKeys";
        final String encryptedText = EncryptionUtil.encrypt(readPublicKey, text);
        final String decryptedText = DecryptionUtil.decrypt(readPrivateKey, encryptedText);
        Assert.assertEquals("Decrypted string is correct", text, decryptedText);
    }

    @Test
    public void testCreateAndReadKeyPair() throws Exception {
        final String userHome = System.getProperty("java.io.tmpdir");
        final String fileName = "/app-front-" + UUID.randomUUID().toString();
        final Path privateKeyPath = Paths.get(userHome + fileName + ".key");
        final Path publicKeyPath = Paths.get(userHome + fileName + ".pub");
        Files.createFile(privateKeyPath);
        Files.createFile(publicKeyPath);

        OutputStream osPrivateKey = null;
        OutputStream osPublicKey = null;
        InputStream isPrivateKey = null;
        InputStream isPublicKey = null;
        try {

            // Create keypair
            osPrivateKey = Files.newOutputStream(privateKeyPath);
            osPublicKey = Files.newOutputStream(publicKeyPath);
            KeyGeneratorUtil.generateKeyPair(osPrivateKey, osPublicKey);
            IOUtils.closeQuietly(osPrivateKey);
            IOUtils.closeQuietly(osPublicKey);

            // Read keypair.
            isPrivateKey = Files.newInputStream(privateKeyPath);
            isPublicKey = Files.newInputStream(publicKeyPath);
            final PrivateKey readPrivateKey = KeyGeneratorUtil.getPrivateKey(isPrivateKey);
            final PublicKey readPublicKey = KeyGeneratorUtil.getPublicKey(isPublicKey);

            // Encrypt & decrypt test
            final String text = "testCreateAndReadKeyPair";
            final String encryptedText = EncryptionUtil.encrypt(readPublicKey, text);
            final String decryptedText = DecryptionUtil.decrypt(readPrivateKey, encryptedText);
            Assert.assertEquals("Decrypted string is correct", text, decryptedText);
        } finally {
            IOUtils.closeQuietly(osPrivateKey);
            IOUtils.closeQuietly(osPublicKey);
            IOUtils.closeQuietly(isPrivateKey);
            IOUtils.closeQuietly(isPublicKey);
        }
    }

    @Test
    public void testEncryptDecrypt() throws Exception {
        final KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
        final String text = "testEncryptDecrypt";

        // Encrypt a string with public key
        final String encryptedText = EncryptionUtil.encrypt(keyPair.getPublic(), text);

        // Decrypt previously encrypted string.
        final String decryptedText = DecryptionUtil.decrypt(keyPair.getPrivate(), encryptedText);

        // Check decrypted string.
        Assert.assertEquals("Decrypted string is correct", text, decryptedText);
    }

    @Test
    public void testEncryptDecryptActivationDto() throws Exception {
        final KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
        final ActivationDTO generatedDto = createActivationDTO();
        final ActivationDTO encryptedDto = EncryptionUtil.encrypt(keyPair.getPublic(), generatedDto);
        Assert.assertNotEquals(generatedDto.getAuthentication().getClientId(), encryptedDto.getAuthentication().getClientId());
        Assert.assertNotEquals(generatedDto.getAuthentication().getClientRandom(), encryptedDto.getAuthentication().getClientRandom());
        final ActivationDTO decryptedDto = DecryptionUtil.decrypt(keyPair.getPrivate(), encryptedDto);
        Assert.assertEquals(generatedDto.getAuthentication().getClientId(), decryptedDto.getAuthentication().getClientId());
        Assert.assertEquals(generatedDto.getAuthentication().getClientRandom(), decryptedDto.getAuthentication().getClientRandom());
        EncryptionUtil.encryptForBackend(generatedDto);
    }

    @Test
    public void testEncryptDecryptAuthenticationDto() throws Exception {
        final KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
        final AuthenticationDTO generatedDto = createAuthenticationDTO();
        final AuthenticationDTO encryptedDto = EncryptionUtil.encrypt(keyPair.getPublic(), generatedDto);
        Assert.assertNotEquals(generatedDto.getClientId(), encryptedDto.getClientId());
        Assert.assertNotEquals(generatedDto.getClientRandom(), encryptedDto.getClientRandom());
        final AuthenticationDTO decryptedDto = DecryptionUtil.decrypt(keyPair.getPrivate(), encryptedDto);
        Assert.assertEquals(generatedDto.getClientId(), decryptedDto.getClientId());
        Assert.assertEquals(generatedDto.getClientRandom(), decryptedDto.getClientRandom());
        EncryptionUtil.encryptForBackend(generatedDto);
    }

    @Test
    public void testEncryptDecryptActivityDto() throws Exception {
        final KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
        final ActivityDTO generatedDto = createActivityDTO();
        final ActivityDTO encryptedDto = EncryptionUtil.encrypt(keyPair.getPublic(), generatedDto);
        Assert.assertNotEquals(generatedDto.getName(), encryptedDto.getName());
        final ActivityDTO decryptedDto = DecryptionUtil.decrypt(keyPair.getPrivate(), encryptedDto);
        Assert.assertEquals(generatedDto.getName(), decryptedDto.getName());
        EncryptionUtil.encryptForBackend(generatedDto);
    }

    @Test
    public void testEncryptDecryptInterestDto() throws Exception {
        final KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
        final InterestDTO generatedDto = createInterestDTO();
        final InterestDTO encryptedDto = EncryptionUtil.encrypt(keyPair.getPublic(), generatedDto);
        Assert.assertNotEquals(generatedDto.getInterestId(), encryptedDto.getInterestId());
        final InterestDTO decryptedDto = DecryptionUtil.decrypt(keyPair.getPrivate(), encryptedDto);
        Assert.assertEquals(generatedDto.getInterestId(), decryptedDto.getInterestId());
        EncryptionUtil.encryptForBackend(generatedDto);
    }

    @Test
    public void testEncryptDecryptMatchDto() throws Exception {
        final KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
        final MatchDTO generatedDto = createMatchDTO();
        final MatchDTO encryptedDto = EncryptionUtil.encrypt(keyPair.getPublic(), generatedDto);
        Assert.assertNotEquals(generatedDto.getLatitude(), encryptedDto.getLatitude());
        Assert.assertNotEquals(generatedDto.getLongitude(), encryptedDto.getLongitude());
        final MatchDTO decryptedDto = DecryptionUtil.decrypt(keyPair.getPrivate(), encryptedDto);
        Assert.assertEquals(generatedDto.getLatitude(), decryptedDto.getLatitude());
        Assert.assertEquals(generatedDto.getLongitude(), decryptedDto.getLongitude());
        EncryptionUtil.encryptForBackend(generatedDto);
    }

    private ActivationDTO createActivationDTO() throws Exception {
        final ActivationDTO activationDTO = new ActivationDTO();
        activationDTO.setAuthentication(createAuthenticationDTO());
        activationDTO.setInterest(createInterestDTO());
        activationDTO.setVisibilityType(MatchType.BLUETOOTH);
        activationDTO.setVisibilityDuration(100);
        return activationDTO;
    }

    private ActivityDTO createActivityDTO() {
        final ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setName("DUMMY_ACTIVITY");
        return activityDTO;
    }

    private AuthenticationDTO createAuthenticationDTO() {
        final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientId(UUID.randomUUID().toString());
        authenticationDTO.setClientRandom(RandomUtil.generateSecureRandom());
        authenticationDTO.setClientPublicKey("PUBLIC_KEY");
        return authenticationDTO;
    }

    private InterestDTO createInterestDTO() {
        final InterestDTO interestDTO = new InterestDTO();
        interestDTO.setInterestId(UUID.randomUUID().toString());
        interestDTO.setInterests(Collections.singleton(createActivityDTO()));
        return interestDTO;
    }

    private MatchDTO createMatchDTO() {
        final MatchDTO matchDTO = new MatchDTO();
        matchDTO.setReason(MatchType.BLUETOOTH);
        matchDTO.setLatitude("4234");
        matchDTO.setLongitude("4323");
        return matchDTO;
    }

}
