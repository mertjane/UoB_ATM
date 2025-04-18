package com.atm.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The {@code TransactionWriter} class provides functionality to log ATM
 * transactions
 * to a CSV file. It records details such as timestamp, account number,
 * transaction type,
 * amount, and resulting balance in a standardized format.
 * <p>
 * <strong>Usage:</strong> This class is used by {@link com.atm.BankAccount} to
 * log
 * deposits and withdrawals to {@code transactions.csv}.
 * </p>
 * <p>
 * <strong>File Format:</strong> Each transaction is written as a
 * comma-separated line:
 * <code>timestamp,accountNumber,type,amount,balance</code>
 * </p>
 * <p>
 * <strong>Version:</strong> Updated in Week 8 to use correct date format
 * {@code yyyy-MM-dd HH:mm:ss}.
 * </p>
 *
 * @author Mertcan (Week 8 implementation)
 */

public class TransactionWriter {
  /** The path to the CSV file where transactions are logged. */
  private static final String FILE_PATH = "transactions.csv";
  /** Formatter for timestamps in the format "yyyy-MM-dd HH:mm:ss". */
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * Logs a transaction to the {@code transactions.csv} file.
   * <p>
   * The transaction is recorded with the current timestamp, account number,
   * transaction type (e.g., "Deposit", "Withdraw"), amount, and resulting
   * balance.
   * The data is appended to the CSV file in a comma-separated format.
   * </p>
   *
   * @param accountNumber the account number associated with the transaction
   * @param type          the type of transaction (e.g., "Deposit", "Withdraw")
   * @param amount        the transaction amount
   * @param balance       the account balance after the transaction
   * @throws RuntimeException if an I/O error occurs while writing to the file
   */

  public static void logTransaction(String accountNumber, String type, double amount, double balance) {
    try (FileWriter writer = new FileWriter(FILE_PATH, true)) { // Append mode
      // Create a comma-separated line with timestamp, account number, type, amount, and balance
      String maskedAccNumber = maskAccountNumber(accountNumber);
      String line = String.join(",",
          formatter.format(LocalDateTime.now()), // Format current time
          maskedAccNumber,
          type,
          String.valueOf(amount), // Convert amount to String
          String.valueOf(balance)); // Convert balance to String
      writer.write(line + "\n"); // Write line to file with newline
    } catch (IOException e) {
      // Log error to stderr if file writing fails
      System.err.println("Failed to log transaction: " + e.getMessage());
    }
  }

   // Helper method to mask account number (show only last 2 digits)
   private static String maskAccountNumber(String accountNumber) {
    if (accountNumber == null || accountNumber.length() <= 2) {
        return accountNumber; // Return as is if too short
    }
    
    int length = accountNumber.length();
    String lastTwoDigits = accountNumber.substring(length - 2);
    return "***" + lastTwoDigits;
}
}
