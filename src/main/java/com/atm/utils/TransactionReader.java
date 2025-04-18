package com.atm.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * The {@code TransactionReader} class retrieves transaction records from a CSV
 * file
 * for a specified account. It reads {@code transactions.csv} and returns a list
 * of
 * transaction lines matching the given account number, limited to a specified
 * count.
 * <p>
 * <strong>Usage:</strong> Used by {@link com.atm.View} to display transaction
 * receipts.
 * </p>
 * <p>
 * <strong>File Format:</strong> Expects lines in the format:
 * <code>timestamp,accountNumber,type,amount,balance</code>
 * </p>
 * <p>
 * <strong>Version:</strong> Week 8 implementation for transaction retrieval.
 * </p>
 *
 * @author Mertcan (Week 8 implementation)
 */

public class TransactionReader {
  /** The path to the CSV file containing transaction records. */
  private static final String FILE_PATH = "transactions.csv";

  /**
   * Retrieves the most recent transactions for a given account number from the
   * {@code transactions.csv} file.
   * <p>
   * Reads the CSV file line by line, collecting lines that contain the specified
   * account number. Returns up to {@code count} most recent matching
   * transactions.
   * </p>
   *
   * @param accountNumber the account number to filter transactions
   * @param count         the maximum number of transactions to return
   * @return a {@code List<String>} of transaction lines, or an empty list if none
   *         found
   * @throws RuntimeException if an I/O error occurs while reading the file
   */
  public static List<String> getTransactions(String accountNumber, int count) {
    List<String> recent = new LinkedList<>(); // List to store matching transactions

    // Check if file exists, if not, return empty list
    File file = new File(FILE_PATH);
    if (!file.exists()) {
      return recent;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) { // Open CSV file
      String line;
      while ((line = reader.readLine()) != null) { // Read each line
        if (line.contains(accountNumber)) { // Check if line contains account number
          recent.add(line); // Add matching line to list
          if (recent.size() > count) { // Limit to 'count' entries
            recent.remove(0); // Remove oldest entry
          }
        }
      }
    } catch (IOException e) {
      // Log error to stderr if file reading fails
      System.err.println("Failed to read transactions: " + e.getMessage());
    }
    return recent; // Return list of transactions
  }

}
