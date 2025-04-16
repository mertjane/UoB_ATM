package com.atm;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for validating the AccountCreator functionality.
 * 
 * <p><strong>Created by:</strong> Bora Sozer - Week 5</p>
 * 
 * <p><strong>Overview:</strong><br>
 * This test class verifies that the AccountCreator properly generates unique account numbers
 * and creates different types of bank accounts with appropriate configurations.</p>
 * 
 * <p><strong>Test Coverage:</strong></p>
 * <ul>
 *   <li>Unique account number generation</li>
 *   <li>Account creation for different account types (Student, Gold, Platinum)</li>
 *   <li>Account type validation</li>
 *   <li>Integration with Bank system</li>
 * </ul>
 * 
 * @author Bora
 * @version 1.0.0
 * @see AccountCreator
 * @see Bank
 * @see BankAccount
 */
public class AccountCreatorTest {
    
    private Bank bank;
    
    /**
     * Sets up a fresh Bank instance before each test.
     */
    @Before
    public void setUp() {
        bank = new Bank();
    }
    
    /**
     * Tests that generated account numbers are unique and in the correct format.
     */
    @Test
    public void testGenerateUniqueAccountNumber() {
        // Generate multiple account numbers and verify they're unique
        String accountNumber1 = AccountCreator.generateUniqueAccountNumber(bank);
        String accountNumber2 = AccountCreator.generateUniqueAccountNumber(bank);
        String accountNumber3 = AccountCreator.generateUniqueAccountNumber(bank);
        
        // Verify uniqueness
        assertNotEquals("Account numbers should be unique", accountNumber1, accountNumber2);
        assertNotEquals("Account numbers should be unique", accountNumber1, accountNumber3);
        assertNotEquals("Account numbers should be unique", accountNumber2, accountNumber3);
        
        // Verify format (5-digit number)
        assertTrue("Account number should be 5 digits", accountNumber1.matches("\\d{5}"));
        assertTrue("Account number should be 5 digits", accountNumber2.matches("\\d{5}"));
        assertTrue("Account number should be 5 digits", accountNumber3.matches("\\d{5}"));
    }
    
    /**
     * Tests that account creation works for all account types.
     */
    @Test
    public void testCreateAccount() {
        // Create accounts of each type
        String studentAccNum = AccountCreator.createAccount(bank, AccountCreator.STUDENT_ACCOUNT, "12345");
        String goldAccNum = AccountCreator.createAccount(bank, AccountCreator.GOLD_ACCOUNT, "12345");
        String platinumAccNum = AccountCreator.createAccount(bank, AccountCreator.PLATINUM_ACCOUNT, "12345");
        
        // Verify accounts were created successfully
        assertNotNull("Student account should be created", studentAccNum);
        assertNotNull("Gold account should be created", goldAccNum);
        assertNotNull("Platinum account should be created", platinumAccNum);
        
        // Verify accounts exist in the bank
        assertTrue("Student account should exist in bank", bank.accountExists(studentAccNum));
        assertTrue("Gold account should exist in bank", bank.accountExists(goldAccNum));
        assertTrue("Platinum account should exist in bank", bank.accountExists(platinumAccNum));
        
        // Verify we can log into the accounts
        assertTrue("Should be able to log into student account", bank.login(studentAccNum, "12345"));
        bank.logout();
        assertTrue("Should be able to log into gold account", bank.login(goldAccNum, "12345"));
        bank.logout();
        assertTrue("Should be able to log into platinum account", bank.login(platinumAccNum, "12345"));
    }
    
    /**
     * Tests that the default account type is created when an invalid type is specified.
     */
    @Test
    public void testCreateAccountWithInvalidType() {
        // Create account with invalid type
        String accountNum = AccountCreator.createAccount(bank, "invalid_type", "12345");
        
        // Verify account was created (should default to student account)
        assertNotNull("Account should be created with default type", accountNum);
        assertTrue("Account should exist in bank", bank.accountExists(accountNum));
        
        // Log in and verify it's a student account by checking withdrawal limit
        bank.login(accountNum, "12345");
        bank.deposit(200);
        assertTrue("Should allow withdrawal within student limit", bank.withdraw(150));
        assertFalse("Should not allow withdrawal beyond student limit", bank.withdraw(151));
    }
    
    /**
     * Tests that account numbers don't conflict with existing accounts.
     */
    @Test
    public void testAccountNumberUniqueness() {
        // Add some accounts to the bank first
        bank.addBankAccount(new StudentAccount("12345", "00000", 0));
        bank.addBankAccount(new GoldAccount("54321", "00000", 0));
        
        // Generate a new account number
        String newAccountNum = AccountCreator.generateUniqueAccountNumber(bank);
        
        // Verify it doesn't conflict with existing accounts
        assertNotEquals("New account number should not conflict with existing accounts", "12345", newAccountNum);
        assertNotEquals("New account number should not conflict with existing accounts", "54321", newAccountNum);
    }
} 