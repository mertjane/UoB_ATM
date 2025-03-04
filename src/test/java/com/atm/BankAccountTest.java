package com.atm;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for validating the internal logic and constraints of different bank account types.
 * 
 * <p><strong>Overview:</strong><br>
 * Created by Bora in Week 4 to perform Quality Assurance testing of the ATM system's account implementations.
 * This test suite was introduced to validate the inheritance-based account structure and ensure
 * proper functionality of all account types and their specific constraints.</p>
 * 
 * <p><strong>Modification History:</strong></p>
 * <ul>
 *   <li><strong>Version 2.0.2 :</strong> Bora
 *     <ul>
 *       <li>Created comprehensive test suite for all account types</li>
 *       <li>Implemented tests for withdrawal/deposit limits</li>
 *       <li>Added overdraft and commission fee validations</li>
 *       <li>Enhanced JavaDoc documentation with detailed scenarios</li>
 *     </ul>
 *   </li>
 *   <li><strong>Version 3.0.1 :</strong> Bora
 *     <ul>
 *       <li>Changed account credentials from int to String to support leading zeros</li>
 *       <li>Updated test account creation to use String format</li>
 *     </ul>
 *   </li>
 * </ul>
 * 
 * <p>This test class focuses on the core functionality of each account type (Student, Gold, Platinum)
 * without involving the bank system integration. It verifies that each account type correctly
 * implements its specific.</p>
 * 
 * <p><strong>Implementation Details:</strong></p>
 * <ul>
 *   <li>Withdrawal limits (e.g., £150 for Student, £2000 for Gold)</li>
 *   <li>Deposit limits (e.g., £250 for Student, £2000 for Gold/Platinum)</li>
 *   <li>Overdraft behavior and limits (e.g., no overdraft for Student, -£1000 for Gold)</li>
 *   <li>Commission fees (e.g., no commission for Student, £0.7 for Platinum)</li>
 * </ul>
 * 
 * <p><strong>Note:</strong> All balance calculations include commission fees for applicable accounts.
 * Commission is applied on both withdrawals and deposits.</p>
 * 
 * @author Original: Bora
 * @version 3.0.1
 * @since 2.0.2
 * @see BankAccount
 * @see StudentAccount
 * @see GoldAccount
 * @see PlatinumAccount
 * @see BankTest
 */
public class BankAccountTest {
    /**
     * Delta value used for comparing double values in balance assertions.
     * Since floating-point arithmetic can result in small rounding errors,
     * we use a small delta (0.01) to allow for these imprecisions.
     * 
     * <p><strong>Example:</strong> When comparing 99.3 and 99.30000001, the test should pass.</p>
     */
    private static final double DELTA = 0.01;

    private StudentAccount studentAccount;
    private GoldAccount goldAccount;
    private PlatinumAccount platinumAccount;
    

    /**
     * Initializes test accounts before each test method.
     * Creates fresh instances of each account type with balances higher than their withdrawal limits
     * to ensure limit testing is not affected by insufficient funds:
     * <ul>
     *   <li>Student Account: £1000 (withdrawal limit: £150)</li>
     *   <li>Gold Account: £3000 (withdrawal limit: £2000)</li>
     *   <li>Platinum Account: £5000 (withdrawal limit: £3000)</li>
     * </ul>
     */
    @Before
    public void setUp() {
        studentAccount = new StudentAccount("00000", "00000", 1000);  // Well above £150 limit
        goldAccount = new GoldAccount("11111", "11111", 3000);        // Well above £2000 limit
        platinumAccount = new PlatinumAccount("22222", "22222", 5000); // Well above £3000 limit
    }

    /**
     * Tests all constraints and behaviors specific to Student accounts.
     * Initial balance (£1000) is set higher than withdrawal limit (£150)
     * to ensure limit testing is not affected by insufficient funds.
     * 
     * <p>Verifies that Student accounts:</p>
     * <ul>
     *   <li>Cannot withdraw more than £150 in a single transaction (due to limit, not balance)</li>
     *   <li>Cannot deposit more than £250 in a single transaction</li>
     *   <li>Cannot go into overdraft (balance must stay ≥ £0)</li>
     *   <li>Do not incur any commission fees on transactions</li>
     * </ul>
     */
    @Test
    public void testStudentAccountFeatures() {
        // Withdrawal limit tests (with sufficient balance)
        assertFalse("Should not allow withdrawal of £151 (limit restriction)", 
            studentAccount.withdraw(151));
        assertTrue("Should allow withdrawal of £150 (within limit)", 
            studentAccount.withdraw(150));
        
        // Overdraft tests
        studentAccount = new StudentAccount("00000", "00000", 100);
        assertFalse("Should not allow overdraft", studentAccount.withdraw(101));
        assertTrue("Should allow withdrawal up to available balance", 
            studentAccount.withdraw(100));
        
        // Deposit limit tests
        assertFalse("Should not allow deposit of £251", studentAccount.deposit(251));
        assertTrue("Should allow deposit of £250", studentAccount.deposit(250));
        
        // Commission test (should be 0)
        double balanceBefore = studentAccount.getBalance();
        studentAccount.withdraw(50);
        assertEquals("Student account should not charge commission", 
            balanceBefore - 50, studentAccount.getBalance(), 0.01);
    }

    /**
     * Tests all constraints and behaviors specific to Gold accounts.
     * Initial balance (£3000) is set higher than withdrawal limit (£2000)
     * to ensure limit testing is not affected by insufficient funds.
     * 
     * <p><strong>Test Scenarios:</strong></p>
     * <ul>
     *   <li>Withdrawal limits: max £2000 per transaction</li>
     *   <li>Deposit limits: max £2000 per transaction</li>
     *   <li>Overdraft: allowed up to -£1000</li>
     *   <li>Commission: £0.5 per transaction (both withdrawals and deposits)</li>
     * </ul>
     * 
     * <p><strong>Balance Calculations:</strong></p>
     * <ul>
     *   <li>Initial balance: £3000</li>
     *   <li>After £2000 withdrawal + £0.5 commission = £999.5</li>
     *   <li>After £2000 deposit - £0.5 commission = £2999.0</li>
     * </ul>
     */
    @Test
    public void testGoldAccountFeatures() {
        // Withdrawal limit tests with balance verification
        assertFalse("Should not allow withdrawal of £2001", goldAccount.withdraw(2001));
        assertTrue("Should allow withdrawal of £2000", goldAccount.withdraw(2000));
        assertEquals("Balance should be £999.5 after £2000 withdrawal and £0.5 commission", 
            999.5, goldAccount.getBalance(), DELTA);
        
        // Deposit tests with balance verification
        assertTrue("Should allow deposit of £2000", goldAccount.deposit(2000));
        assertEquals("Balance should be £2999.0 after £2000 deposit and £0.5 commission deduction", 
            2999.0, goldAccount.getBalance(), DELTA);
        assertFalse("Should not allow deposit of £2001", goldAccount.deposit(2001));
        
        // Overdraft tests
        goldAccount = new GoldAccount("11111", "11111", 500);
        assertTrue("Should allow withdrawal up to overdraft limit", goldAccount.withdraw(1000));
        assertEquals("Balance should be -£500.5 after withdrawal and commission", 
            -500.5, goldAccount.getBalance(), DELTA);
        assertFalse("Should not allow exceeding overdraft limit of -£1000", 
            goldAccount.withdraw(500)); // Would exceed -£1000 limit
        
        // Additional commission test for clarity
        goldAccount = new GoldAccount("11111", "11111", 100);
        assertTrue("Should allow small withdrawal", goldAccount.withdraw(10));
        assertEquals("Balance should be £89.5 after £10 withdrawal and £0.5 commission",
            89.5, goldAccount.getBalance(), DELTA);
    }

    /**
     * Tests all constraints and behaviors specific to Platinum accounts.
     * Initial balance (£5000) is set higher than withdrawal limit (£3000)
     * to ensure limit testing is not affected by insufficient funds.
     * 
     * <p>Verifies that Platinum accounts:</p>
     * <ul>
     *   <li>Cannot withdraw more than £3000 in a single transaction (due to limit, not balance)</li>
     *   <li>Cannot deposit more than £2000 in a single transaction</li>
     *   <li>Can go into overdraft up to -£1500</li>
     *   <li>Incur a £0.7 commission fee per transaction</li>
     * </ul>
     */
    @Test
    public void testPlatinumAccountFeatures() {
        // Withdrawal limit tests (with sufficient balance)
        assertFalse("Should not allow withdrawal of £3001 (limit restriction)", 
            platinumAccount.withdraw(3001));
        assertTrue("Should allow withdrawal of £3000 (within limit)", 
            platinumAccount.withdraw(3000));
        
        // Overdraft tests
        platinumAccount = new PlatinumAccount("22222", "22222", 1000);
        assertTrue("Should allow withdrawal up to overdraft limit", 
            platinumAccount.withdraw(2000));  // Will result in -£1500 balance
        assertFalse("Should not allow exceeding overdraft limit of -£1500", 
            platinumAccount.withdraw(600));   // Would exceed -£1500 limit
        
        // Deposit limit tests
        assertFalse("Should not allow deposit of £2001", platinumAccount.deposit(2001));
        assertTrue("Should allow deposit of £2000", platinumAccount.deposit(2000));
        
        // Commission test (should be 0.7)
        platinumAccount = new PlatinumAccount("22222", "22222", 1000);
        assertTrue(platinumAccount.withdraw(100));
        assertEquals("Should apply £0.7 commission", 
            899.3, platinumAccount.getBalance(), 0.01);
    }
} 