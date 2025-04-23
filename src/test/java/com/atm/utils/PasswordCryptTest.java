package com.atm.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Test class for the {@link PasswordCrypt} utility.
 * <p>
 * This class contains unit tests for verifying the encryption and decryption
 * functionality of the PasswordCrypt class, ensuring that sensitive data
 * can be securely stored and retrieved.
 * </p>
 * <p>
 * <strong>Created by:</strong> Bora Sozer - Week 8 (QA Implementation)
 * </p>
 * <p>
 * These tests verify that:
 * <ul>
 *   <li>Encryption produces different output than the original input</li>
 *   <li>Decryption of encrypted data returns the original input</li>
 *   <li>The system handles various input types correctly (numbers, special characters, etc.)</li>
 *   <li>Empty strings are handled appropriately</li>
 * </ul>
 * </p>
 * 
 * @author Bora Sozer
 * @version 1.0.0
 */
public class PasswordCryptTest {

    @Test
    @DisplayName("Test that encryption changes the original string")
    public void testEncryptionChangesInput() {
        String original = "testPassword123";
        String encrypted = PasswordCrypt.encrypt(original);
        
        assertNotEquals(original, encrypted, "Encrypted string should be different from original");
        assertFalse(encrypted.contains(original), "Encrypted string should not contain the original password");
    }
    
    @Test
    @DisplayName("Test that decryption returns the original string")
    public void testDecryptionReturnsOriginal() {
        String original = "securePassword!@#";
        String encrypted = PasswordCrypt.encrypt(original);
        String decrypted = PasswordCrypt.decrypt(encrypted);
        
        assertEquals(original, decrypted, "Decrypted string should match the original");
    }
    
    @Test
    @DisplayName("Test encryption and decryption with special characters")
    public void testWithSpecialCharacters() {
        String original = "P@$$w0rd!#%^&*()_+";
        String encrypted = PasswordCrypt.encrypt(original);
        String decrypted = PasswordCrypt.decrypt(encrypted);
        
        assertEquals(original, decrypted, "Special characters should be preserved after encryption and decryption");
    }
    
    @Test
    @DisplayName("Test encryption and decryption with empty string")
    public void testWithEmptyString() {
        String original = "";
        String encrypted = PasswordCrypt.encrypt(original);
        String decrypted = PasswordCrypt.decrypt(encrypted);
        
        assertEquals(original, decrypted, "Empty string should be handled correctly");
    }
    
    @Test
    @DisplayName("Test encryption and decryption with numeric string")
    public void testWithNumericString() {
        String original = "12345678901234567890";
        String encrypted = PasswordCrypt.encrypt(original);
        String decrypted = PasswordCrypt.decrypt(encrypted);
        
        assertEquals(original, decrypted, "Numeric strings should be preserved after encryption and decryption");
    }
} 