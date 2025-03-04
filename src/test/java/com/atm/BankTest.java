package com.atm;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for Bank operations and system-level functionality.
 * 
 * <p><strong>Overview:</strong><br>
 * Created by Bora in Week 4 to test the bank system's integration with different account types.
 * This test suite focuses on system-level operations rather than individual account behaviors,
 * which are covered in {@link BankAccountTest}.</p>
 * 
 * <p><strong>Test Coverage:</strong></p>
 * <ul>
 *   <li>Authentication (login/logout security)</li>
 *   <li>Session management</li>
 *   <li>Transaction persistence across sessions</li>
 *   <li>System-level balance tracking</li>
 * </ul>
 * 
 * <p><strong>Modification History:</strong></p>
 * <ul>
 *   <li><strong>Version 2.0.2 :</strong> Bora
 *     <ul>
 *       <li>Created system-level test suite</li>
 *       <li>Implemented authentication tests</li>
 *       <li>Added transaction routing tests</li>
 *       <li>Enhanced session management validation</li>
 *     </ul>
 *   </li>
 *   <li><strong>Version 3.0.1 :</strong> Bora
 *     <ul>
 *       <li>Changed account credentials from int to String to support leading zeros</li>
 *       <li>Modified test account creation to use String credentials</li>
 *       <li>Added leading zero support test</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * @author Original: Bora
 * @version 3.0.1
 * @since 2.0.2
 * @see Bank
 * @see BankAccount
 * @see BankAccountTest
 */
@RunWith(JUnit4.class)
public class BankTest {
    private Bank bank;
    private static final double DELTA = 0.01;

    /**
     * Sets up test environment before each test.
     * Creates a fresh bank instance with three different types of accounts
     * to test system-level interactions.
     */
    @Before
    public void setUp() {
        bank = new Bank();
        bank.addBankAccount(new StudentAccount("00000", "00000", 1000));
        bank.addBankAccount(new GoldAccount("11111", "11111", 2000));
        bank.addBankAccount(new PlatinumAccount("22222", "22222", 3000));
    }

    /**
     * Tests authentication and session management.
     * Verifies login success/failure and post-logout security.
     */
    @Test
    public void testAuthentication() {
        // Valid login tests
        assertTrue("Should allow valid student account login", bank.login("00000", "00000"));
        bank.logout();
        assertTrue("Should allow valid gold account login", bank.login("11111", "11111"));
        
        // Invalid login tests
        assertFalse("Should reject invalid account number", bank.login("99999", "00000"));
        assertFalse("Should reject invalid password", bank.login("00000", "99999"));
        
        // Session security tests
        bank.login("00000", "00000");
        bank.logout();
        assertFalse("Should prevent operations after logout", bank.withdraw(100));
        assertFalse("Should prevent balance check after logout", bank.getBalance() > 0);
    }

    /**
     * Tests transaction routing to correct accounts.
     * Verifies that transactions are processed by the correct account
     * and balances are properly tracked at system level.
     */
    @Test
    public void testTransactionRouting() {
        // Student account transactions
        bank.login("00000", "00000");
        assertTrue("Should route deposit to student account", bank.deposit(100));
        assertEquals("Should track student account balance", 1100, bank.getBalance(), DELTA);
        bank.logout();
        
        // Gold account transactions
        bank.login("11111", "11111");
        assertTrue("Should route withdrawal to gold account", bank.withdraw(500));
        assertEquals("Should track gold account balance with commission", 
            1499.5, bank.getBalance(), DELTA);
        bank.logout();
        
        // Platinum account transactions
        bank.login("22222", "22222");
        assertTrue("Should route deposit to platinum account", bank.deposit(1000));
        assertEquals("Should track platinum account balance with commission", 
            3999.3, bank.getBalance(), DELTA);
    }

    /**
     * Tests proper account access and operations.
     * Verifies that each login session accesses the correct account
     * and transactions are processed with proper commission rules.
     */
    @Test
    public void testAccountOperations() {
        // Student account operations
        bank.login("00000", "00000");
        assertTrue("Should allow student account deposit", bank.deposit(100));
        assertEquals("Should update student account balance", 
            1100, bank.getBalance(), DELTA);
        bank.logout();
        
        // Gold account operations
        bank.login("11111", "11111");
        assertTrue("Should allow gold account withdrawal", bank.withdraw(500));
        assertEquals("Should update gold account balance with commission", 
            1499.5, bank.getBalance(), DELTA);
        bank.logout();
        
        // Platinum account operations
        bank.login("22222", "22222");
        assertTrue("Should allow platinum account deposit", bank.deposit(1000));
        assertEquals("Should update platinum account balance with commission", 
            3999.3, bank.getBalance(), DELTA);
    }

    /**
     * Tests session-based account access security and transaction persistence.
     * Verifies that:
     * <ul>
     *   <li>After logout, previous account data cannot be accessed</li>
     *   <li>Each login provides access to the correct account balance</li>
     *   <li>Transactions persist across multiple login sessions</li>
     * </ul>
     */
    @Test
    public void testSessionSecurity() {
        // Initial transaction
        bank.login("11111", "11111");
        double initialBalance = bank.getBalance();
        assertTrue("Should complete withdrawal", bank.withdraw(500));
        assertEquals("Balance should be reduced by 500.5", 
            initialBalance - 500.5, bank.getBalance(), DELTA);
        bank.logout();
        
        // Verify transaction persists after re-login
        bank.login("11111", "11111");
        assertEquals("Balance should remain updated after re-login", 
            initialBalance - 500.5, bank.getBalance(), DELTA);
        bank.logout();
        
        // Verify other account remains unaffected
        bank.login("00000", "00000");
        assertEquals("Student account should remain at initial balance", 
            1000, bank.getBalance(), DELTA);
        bank.logout();
        
        // Multiple session persistence test
        bank.login("11111", "11111");
        assertTrue("Should complete deposit", bank.deposit(200));
        double afterDeposit = bank.getBalance();
        bank.logout();
        
        bank.login("11111", "11111");
        assertEquals("Balance should reflect all transactions after multiple sessions", 
            afterDeposit, bank.getBalance(), DELTA);
    }

    /**
     * Tests leading zero support in account credentials.
     * Verifies that accounts with leading zeros in their credentials
     * can be properly authenticated and accessed.
     * <p>
     * Week 5 - Bora - Version 3.0.1: Added to validate the leading zero support.
     * </p>
     */
    @Test
    public void testLeadingZeroSupport() {
        // Create account with leading zeros
        bank.addBankAccount(new StudentAccount("00123", "00456", 500));
        
        // Test login with leading zeros
        assertTrue("Should authenticate with leading zeros", bank.login("00123", "00456"));
        assertEquals("Should access correct account balance", 500, bank.getBalance(), DELTA);
        
        // Test that zeros are significant
        bank.logout();
        assertFalse("Should not authenticate if leading zeros are omitted", bank.login("123", "456"));
        
        // Test with different number of leading zeros
        bank.logout();
        assertFalse("Should not authenticate with incorrect number of zeros", bank.login("000123", "00456"));
    }
} 