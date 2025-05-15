package com.atm.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * utility class responsible for reading account data from a csv file.
 * <p>
 * supports loading all accounts and finding a specific account by account number.
 * passwords are decrypted before returning.
 *
 * @author mertcan
 * @version week 8 implementation
 */
public class AccountReader {
    // file path for the accounts csv file
    private static final String FILE_PATH = "accounts.csv";
   

     /**
     * reads all account records from the csv file.
     * <p>
     * each line in the file should contain: accNumber, encryptedPassword, accType, balance.
     * the method decrypts the password before returning.
     *
     * @return a list of account data arrays (each array has 4 strings)
     */
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
                // parse csv line into array
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    // decrypt password (acc number remains as plain for now)
                    /* String decryptedAccNumber = PasswordCrypt.decrypt(parts[0]); */
                    String accNumber = parts[0];
                    String decryptedPassword = PasswordCrypt.decrypt(parts[1]);
                    String accType = parts[2];
                    String balance = parts[3];

                   // add account to list
                    accounts.add(new String[]{accNumber, decryptedPassword, accType, balance});
                }
            }
        } catch (IOException e) {
            // Log error but don't throw exception that would crash the app
            System.err.println("Error reading account data: " + e.getMessage());
        }
        return accounts;
    }

    /**
     * searches the csv file for an account with a matching account number.
     *
     * @param accNumber the account number to search for
     * @return the account as a string array if found, otherwise null
     */
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