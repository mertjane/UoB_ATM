package com.atm.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * utility class for handling encryption and decryption of passwords
 * using aes algorithm with a static secret key.
 * <p>
 * note: the secret key is hardcoded and should be handled more securely in production.
 * <strong>Version:</strong> Week 8 implementation for PasswordEncryption.
 * </p>
 *
 * @author Mertcan (Week 8 implementation)
 */

public class PasswordCrypt {
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "UoBATMSecretKey1"; // 16 characters for AES-128


    /**
     * encrypts the given plain text using aes encryption.
     *
     * @param data the plain text to encrypt
     * @return base64 encoded encrypted string
     */
    public static String encrypt(String data) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            // encryption failed, rethrowing as runtime exception
            throw new RuntimeException("Error encrypting data", e);
        }
    }


    /**
     * decrypts the given base64 encoded encrypted string back to plain text.
     *
     * @param encryptedData the base64 encoded encrypted string
     * @return decrypted plain text
     */
    public static String decrypt(String encryptedData) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // decryption failed, throwing runtime exception
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}
