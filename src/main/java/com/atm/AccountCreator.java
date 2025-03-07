package com.atm;

import java.util.Random;

/**
 * The AccountCreator class handles the creation of new bank accounts.
 * It provides functionality for generating unique account numbers
 * and creating different types of bank accounts (Student, Gold, Platinum).
 * <p>
 * Created by Bora in Week 5 version 3.0.4 as a utility class to manage
 * account creation process, supporting multiple account types and automatic
 * account number generation.
 * </p>
 * 
 * <p><strong>Modification History:</strong><br>
 * Add your modifications here with version numbers and descriptions.
 * </p>
 */
public class AccountCreator {
    
    /** Random number generator for account numbers */
    private static final Random random = new Random();
    
    /** Account type constants */
    public static final String STUDENT_ACCOUNT = "student";
    public static final String GOLD_ACCOUNT = "gold";
    public static final String PLATINUM_ACCOUNT = "platinum";
    
    /**
     * Generates a random 5-digit account number that doesn't exist in the bank.
     * 
     * @param bank The bank to check for existing account numbers
     * @return A unique 5-digit account number as a string
     */
    public static String generateUniqueAccountNumber(Bank bank) {
        String accountNumber;
        do {
            // Generate a random 5-digit number
            int num = 10000 + random.nextInt(90000);
            accountNumber = String.valueOf(num);
        } while (bank.accountExists(accountNumber));
        
        return accountNumber;
    }
    
    /**
     * Creates a new bank account of the specified type.
     * 
     * @param bank The bank to add the account to
     * @param accountType The type of account to create (student, gold, platinum)
     * @param password The password for the new account
     * @return The account number of the newly created account, or null if creation failed
     */
    public static String createAccount(Bank bank, String accountType, String password) {
        // Generate a unique account number
        String accountNumber = generateUniqueAccountNumber(bank);
        
        // Create the appropriate account type
        BankAccount newAccount;
        switch (accountType.toLowerCase()) {
            case STUDENT_ACCOUNT:
                newAccount = new StudentAccount(accountNumber, password, 0);
                break;
            case GOLD_ACCOUNT:
                newAccount = new GoldAccount(accountNumber, password, 0);
                break;
            case PLATINUM_ACCOUNT:
                newAccount = new PlatinumAccount(accountNumber, password, 0);
                break;
            default:
                // Default to student account if type is unknown
                newAccount = new StudentAccount(accountNumber, password, 0);
        }
        
        // Add the account to the bank
        if (bank.addBankAccount(newAccount)) {
            Debug.trace("AccountCreator::createAccount: Created new " + accountType + 
                    " account with number " + accountNumber);
            return accountNumber;
        } else {
            Debug.trace("AccountCreator::createAccount: Failed to create account");
            return null;
        }
    }
} 