package com.atm.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for the transaction storage utilities {@link TransactionReader} and {@link TransactionWriter}.
 * <p>
 * This class contains unit tests for verifying the functionality of logging
 * and retrieving transaction records, ensuring that transaction data is
 * properly recorded and can be accessed for reporting purposes.
 * </p>
 * <p>
 * <strong>Created by:</strong> Bora Sozer - Week 8 (QA Implementation)
 * </p>
 * <p>
 * These tests verify that:
 * <ul>
 *   <li>Transactions can be logged to the storage file</li>
 *   <li>Transactions can be retrieved by account number</li>
 *   <li>The system correctly limits the number of transactions returned</li>
 *   <li>Transaction records contain the expected data in the correct format</li>
 * </ul>
 * </p>
 * 
 * @author Bora Sozer
 * @version 1.0.0
 */
public class TransactionStorageTest {
    
    private static final String FILE_PATH = "transactions.csv";
    private static final String BACKUP_FILE_PATH = "transactions_backup.csv";
    private File transactionFile;
    private boolean backupCreated = false;
    
    @BeforeEach
    public void setUp() {
        // Backup existing transaction file if it exists
        transactionFile = new File(FILE_PATH);
        if (transactionFile.exists()) {
            try {
                Files.copy(Paths.get(FILE_PATH), Paths.get(BACKUP_FILE_PATH), StandardCopyOption.REPLACE_EXISTING);
                backupCreated = true;
            } catch (Exception e) {
                System.err.println("Failed to backup transaction file: " + e.getMessage());
            }
        }
    }
    
    @AfterEach
    public void tearDown() {
        // Restore transaction file from backup if backup was created
        if (backupCreated) {
            try {
                Files.copy(Paths.get(BACKUP_FILE_PATH), Paths.get(FILE_PATH), StandardCopyOption.REPLACE_EXISTING);
                Files.delete(Paths.get(BACKUP_FILE_PATH));
            } catch (Exception e) {
                System.err.println("Failed to restore transaction file: " + e.getMessage());
            }
        }
    }
    
    @Test
    @DisplayName("Test logging and retrieving a transaction")
    public void testLogAndRetrieveTransaction() {
        // Generate a unique test account number to avoid conflicts
        String testAccNumber = "TEST" + System.currentTimeMillis();
        String type = "Deposit";
        double amount = 100.50;
        double balance = 500.75;
        
        // Log a test transaction
        TransactionWriter.logTransaction(testAccNumber, type, amount, balance);
        
        // Retrieve transactions and verify
        List<String> transactions = TransactionReader.getTransactions(testAccNumber, 10);
        
        // Verify we found at least one transaction for our test account
        assertTrue(transactions.size() >= 1, "Should have retrieved at least one transaction");
        
        // Find our test transaction
        boolean foundTestTransaction = false;
        for (String transaction : transactions) {
            if (transaction.contains(testAccNumber) && 
                transaction.contains(type) && 
                transaction.contains(String.valueOf(amount)) && 
                transaction.contains(String.valueOf(balance))) {
                
                foundTestTransaction = true;
                String[] parts = transaction.split(",");
                assertEquals(5, parts.length, "Transaction should have 5 parts");
                assertEquals(testAccNumber, parts[1], "Account number should match");
                assertEquals(type, parts[2], "Transaction type should match");
                assertEquals(String.valueOf(amount), parts[3], "Amount should match");
                assertEquals(String.valueOf(balance), parts[4], "Balance should match");
                break;
            }
        }
        
        assertTrue(foundTestTransaction, "Should have found the test transaction");
    }
    
    @Test
    @DisplayName("Test transaction retrieval limit")
    public void testTransactionLimit() {
        // Generate a unique test account number
        String testAccNumber = "LIMIT" + System.currentTimeMillis();
        
        // Log multiple transactions for this test account
        for (int i = 1; i <= 5; i++) {
            TransactionWriter.logTransaction(testAccNumber, "Test", i * 100.0, i * 100.0);
        }
        
        // Retrieve with a limit of 3
        List<String> transactions = TransactionReader.getTransactions(testAccNumber, 3);
        
        // Should have at most 3 transactions
        assertTrue(transactions.size() <= 3, "Should have retrieved at most 3 transactions");
        
        // Verify we got the most recent transactions (with the highest amounts)
        boolean foundLatestTransactions = false;
        for (String transaction : transactions) {
            if (transaction.contains(testAccNumber) && transaction.contains("500.0")) {
                foundLatestTransactions = true;
                break;
            }
        }
        
        assertTrue(foundLatestTransactions, "Should have found the most recent transactions");
    }
    
    @Test
    @DisplayName("Test retrieving transactions for non-existent account")
    public void testNonExistentAccount() {
        // Use a very unlikely account number
        String nonExistentAccount = "NONEXISTENT" + System.currentTimeMillis();
        
        // Try to retrieve transactions
        List<String> transactions = TransactionReader.getTransactions(nonExistentAccount, 10);
        
        // Should return an empty list
        assertTrue(transactions.isEmpty(), "Should return an empty list for non-existent account");
    }
} 