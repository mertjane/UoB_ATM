package com.atm.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AccountWriter {
    private static final String FILE_PATH = "accounts.csv";

    public static void writeAccount(String accNumber, String accPassword, String accType, double balance) {
        ensureFileExists();
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            // Encrypt sensitive data
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

    public static void writeAccounts(List<String[]> accounts) {
        ensureFileExists();
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (String[] account : accounts) {
                if (account.length >= 4) {
                    /* String encryptedAccNumber = PasswordCrypt.encrypt(account[0]); */
                    
                    String encryptedPassword = PasswordCrypt.encrypt(account[1]);
                    writer.println(String.format("%s,%s,%s,%s",
                            account[0],
                            encryptedPassword,
                            account[2],
                            account[3]));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing accounts data", e);
        }
    }

    // Helper method to ensure the file exists
    private static void ensureFileExists() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating accounts file: " + e.getMessage());
            }
        }
    }
}