package com.atm.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AccountReader {
    private static final String FILE_PATH = "accounts.csv";
    
    // Map to store original account numbers (memory-only, not persisted)
    private static final Map<String, String> accountNumberMap = new HashMap<>();
    
    public static List<String[]> readAccounts() {
        List<String[]> accounts = new ArrayList<>();
        File file = new File(FILE_PATH);

        if(!file.exists()) {
            return accounts;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    // Decrypt sensitive data
                    String accountNumber = parts[0];
                    // String decryptedAccNumber = PasswordCrypt.decrypt(parts[0]);
                    String decryptedPassword = PasswordCrypt.decrypt(parts[1]);
                    String accType = parts[2];
                    String balance = parts[3];

                    // Use the mapping if available, otherwise use masked number
                    String displayAccNumber = accountNumberMap.getOrDefault(accountNumber, accountNumber);
                    
                    accounts.add(new String[]{displayAccNumber, decryptedPassword, accType, balance});
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading account data", e);
        }
        return accounts;
    }

    public static String[] findAccount(String accNumber) {
         // First, check if we need to consider the masked version
         String maskedVersion = "***" + accNumber.substring(Math.max(0, accNumber.length() - 2));

        List<String[]> accounts = readAccounts();
        for (String[] account : accounts) {
            if (account[0].equals(accNumber) || account[0].equals(maskedVersion)) {
                return account;
            }
        }
        return null;
    }

     // Method to register an original account number for lookup
     public static void registerAccountNumber(String originalAccNumber) {
        if (originalAccNumber != null && originalAccNumber.length() > 2) {
            String maskedVersion = "***" + originalAccNumber.substring(originalAccNumber.length() - 2);
            accountNumberMap.put(maskedVersion, originalAccNumber);
        }
    }
}
