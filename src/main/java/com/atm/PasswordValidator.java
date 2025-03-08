package com.atm;

/**
 * Utility class for password validation operations.
 * <p>
 * Created by Bora in Week 5 version 3.0.5 to centralize password validation
 * logic and improve code organization.
 * </p>
 * 
 * <p><strong>Modification History:</strong><br>
 * Add your modifications here with version numbers and descriptions.
 * </p>
 */
public class PasswordValidator {
    /** Minimum required length for passwords */
    public static final int MIN_PASSWORD_LENGTH = 4;
    
    /** Maximum allowed length for passwords */
    public static final int MAX_PASSWORD_LENGTH = 5;
    
    /**
     * Validates if the given password meets the length requirements.
     *
     * @param password The password to validate
     * @return true if password length is valid, false otherwise
     */
    public static boolean isValidLength(String password) {
        return password != null && 
               password.length() >= MIN_PASSWORD_LENGTH && 
               password.length() <= MAX_PASSWORD_LENGTH;
    }
    
    /**
     * Gets the error message for invalid password length.
     *
     * @return The error message string
     */
    public static String getLengthErrorMessage() {
        return "Password must be " + MIN_PASSWORD_LENGTH + " to " + 
               MAX_PASSWORD_LENGTH + " digits";
    }
} 