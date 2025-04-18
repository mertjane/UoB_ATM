package com.atm;

import java.util.Random;

import com.atm.utils.AccountReader;
import com.atm.utils.AccountWriter;

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
    
    /** Student account type with standard features and no monthly fees. */
    public static final String STUDENT_ACCOUNT = "student";
    
    /** Gold account type offering premium features with moderate monthly fees. */
    public static final String GOLD_ACCOUNT = "gold";
    
    /** Platinum account type providing exclusive features with higher monthly fees. */
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
        } while (bank.accountExists(accountNumber) || accountExistsInStorage(accountNumber));
        
        return accountNumber;
    }
    
    private static boolean accountExistsInStorage(String accNumber) {
        return AccountReader.findAccount(accNumber) != null;
    }
    
    /**
     * Creates a new account in the bank with the specified account type and password.
     *
     * @param bank        the Bank in which to create the account
     * @param accountType the type of account to create (STUDENT_ACCOUNT, GOLD_ACCOUNT, or
     *                    PLATINUM_ACCOUNT)
     * @param password    the password for the new account
     * @return the account number of the newly created account, or null if creation failed
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
        
        // Add the account to the bank and save to storage
        if (bank.addBankAccount(newAccount)) {
            // Also save to persistent storage
            AccountWriter.writeAccount(accountNumber, password, accountType.toLowerCase(), 0);
            Debug.trace("AccountCreator::createAccount: Created new " + accountType + 
                    " account with number " + accountNumber);
            return accountNumber;
        } else {
            Debug.trace("AccountCreator::createAccount: Failed to create account");
            return null;
        }
    }
} 