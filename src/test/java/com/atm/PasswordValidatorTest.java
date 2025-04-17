package com.atm;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for validating the PasswordValidator functionality.
 * 
 * <p><strong>Created by:</strong> Bora Sozer - Week 5</p>
 * 
 * <p><strong>Overview:</strong><br>
 * This test class verifies that the PasswordValidator correctly validates
 * passwords according to the defined length requirements.</p>
 * 
 * <p><strong>Test Coverage:</strong></p>
 * <ul>
 *   <li>Password length validation</li>
 *   <li>Edge cases (minimum and maximum lengths)</li>
 *   <li>Invalid inputs (null, empty, too short, too long)</li>
 *   <li>Error message consistency</li>
 * </ul>
 * 
 * @author Bora
 * @version 1.0.0
 * @see PasswordValidator
 */
public class PasswordValidatorTest {
    
    /**
     * Tests that valid passwords pass validation.
     */
    @Test
    public void testValidPasswords() {
        // Test minimum length password
        assertTrue("Password of minimum length should be valid", 
                PasswordValidator.isValidLength("1234"));
        
        // Test maximum length password
        assertTrue("Password of maximum length should be valid", 
                PasswordValidator.isValidLength("12345"));
        
        // Test password between min and max length
        assertTrue("Password between min and max length should be valid", 
                PasswordValidator.isValidLength("1234"));
    }
    
    /**
     * Tests that invalid passwords fail validation.
     */
    @Test
    public void testInvalidPasswords() {
        // Test null password
        assertFalse("Null password should be invalid", 
                PasswordValidator.isValidLength(null));
        
        // Test empty password
        assertFalse("Empty password should be invalid", 
                PasswordValidator.isValidLength(""));
        
        // Test too short password
        assertFalse("Password shorter than minimum length should be invalid", 
                PasswordValidator.isValidLength("123"));
        
        // Test too long password
        assertFalse("Password longer than maximum length should be invalid", 
                PasswordValidator.isValidLength("123456"));
    }
    
    /**
     * Tests that the error message contains the correct length requirements.
     */
    @Test
    public void testErrorMessage() {
        String errorMessage = PasswordValidator.getLengthErrorMessage();
        
        // Verify error message contains the minimum length
        assertTrue("Error message should mention minimum length", 
                errorMessage.contains(String.valueOf(PasswordValidator.MIN_PASSWORD_LENGTH)));
        
        // Verify error message contains the maximum length
        assertTrue("Error message should mention maximum length", 
                errorMessage.contains(String.valueOf(PasswordValidator.MAX_PASSWORD_LENGTH)));
    }
    
    /**
     * Tests that the validation constants are consistent with each other.
     */
    @Test
    public void testValidationConstants() {
        // Verify that MIN_PASSWORD_LENGTH is less than or equal to MAX_PASSWORD_LENGTH
        assertTrue("Minimum password length should be less than or equal to maximum length",
                PasswordValidator.MIN_PASSWORD_LENGTH <= PasswordValidator.MAX_PASSWORD_LENGTH);
        
        // Verify that MIN_PASSWORD_LENGTH is positive
        assertTrue("Minimum password length should be positive",
                PasswordValidator.MIN_PASSWORD_LENGTH > 0);
    }
} 