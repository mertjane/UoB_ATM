package com.atm.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * utility class for writing account information to a csv file.
 * <p>
 * account records are stored in a file named "accounts.csv" and include
 * account number, encrypted password, account type, and balance.
 * <p>
 * note: passwords are encrypted before being saved for basic security.
 * the file is created automatically if it doesn't exist.
 */

public class AccountWriter {
    // file path to the csv file storing account data
    private static final String FILE_PATH = "accounts.csv";


    /**
     * writes a single account record to the csv file.
     * appends to the file if it already exists.
     *
     * @param accNumber   the account number (currently not encrypted)
     * @param accPassword the plain text password to be encrypted before writing
     * @param accType     the type of account (e.g., "savings", "checking")
     * @param balance     the account balance to be stored
     */
    public static void writeAccount(String accNumber, String accPassword, String accType, double balance) {
        ensureFileExists();
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            // encrypt only the password for now
            /* String encryptedAccNumber = PasswordCrypt.encrypt(accNumber); */
            String encryptedPassword = PasswordCrypt.encrypt(accPassword);

            // Write account details in CSV format
            writer.println(String.format("%s,%s,%s,%.2f",
                    accNumber,
                    encryptedPassword,
                    accType,
                    balance));
        } catch (IOException e) {
            throw new RuntimeException("Error writing account data", e);
        }
    }

     /**
     * writes a list of account records to the csv file.
     * this method overwrites the existing file content.
     *
     * @param accounts a list of string arrays, each representing an account.
     *                 expected format: [accNumber, accPassword, accType, balance]
     */
    public static void writeAccounts(List<String[]> accounts) {
        ensureFileExists();
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (String[] account : accounts) {
                if (account.length >= 4) {
                    // only encrypt the password (index 1)
                    /* String encryptedAccNumber = PasswordCrypt.encrypt(account[0]); */
                    
                    String encryptedPassword = PasswordCrypt.encrypt(account[1]);
                    writer.println(String.format("%s,%s,%s,%s",
                            account[0], // acc number
                            encryptedPassword,
                            account[2], // acc type
                            account[3])); // balance
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing accounts data", e);
        }
    }

    /**
     * ensures that the csv file exists before writing.
     * if the file or parent directory does not exist, it will be created.
     */
    private static void ensureFileExists() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs(); // create parent directories if missing
                }
                file.createNewFile(); // create empty file
            } catch (IOException e) {
                System.err.println("Error creating accounts file: " + e.getMessage());
            }
        }
    }
}