package com.atm.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountReader {
    private static final String FILE_PATH = "accounts.csv";
   
    public static List<String[]> readAccounts() {
        List<String[]> accounts = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        // Check if file exists, if not, return empty accounts list
        if (!file.exists()) {
            return accounts;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    // Decrypt sensitive data
                    /* String decryptedAccNumber = PasswordCrypt.decrypt(parts[0]); */
                    String accNumber = parts[0];
                    String decryptedPassword = PasswordCrypt.decrypt(parts[1]);
                    String accType = parts[2];
                    String balance = parts[3];
                   
                    accounts.add(new String[]{accNumber, decryptedPassword, accType, balance});
                }
            }
        } catch (IOException e) {
            // Log error but don't throw exception that would crash the app
            System.err.println("Error reading account data: " + e.getMessage());
        }
        return accounts;
    }
    
    public static String[] findAccount(String accNumber) {
        List<String[]> accounts = readAccounts();
        for (String[] account : accounts) {
            if (account[0].equals(accNumber)) {
                return account;
            }
        }
        return null;
    }
}