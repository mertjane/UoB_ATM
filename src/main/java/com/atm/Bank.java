package com.atm;

import java.util.ArrayList;

/**
 * The Bank class is a simple implementation of a bank that manages a collection of bank accounts.
 * <p>
 * It maintains an internal list of {@link BankAccount} objects and tracks the currently logged-in account.
 * </p>
 * <p>
 * <strong>Version and Modification History:</strong><br>
 * - Mertcan Task 2 Update 2.0.0: Initial implementation for bank account management.<br>
 * - Gur Task Week 4 version 2.0.1: Modified the deposit, withdraw, getBalance, and getLastMessage methods
 *   to integrate updated account logic.
 * - Bora Task Week 5 version 3.0.1: Modified the login method to use String parameters for account number and password.
 * - Bora Task Week 5 version 3.0.2: Added changePassword method to support password changing functionality.
 * - Bora Task Week 5 version 3.0.4: Implemented account creation process and integrated with AccountCreator class.
 * </p>
 * <p>
 * Note: The {@code login} method is partially implemented as part of the lab exercise. Tutors can help with guidance.
 * </p>
 */
public class Bank
{
    // Instance variables containing the bank information
    int maxAccounts = 10;       // maximum number of accounts the bank can hold
    int numAccounts = 0;        // the number of accounts currently in the bank

    // Week 2 implementation
    // Implemented by Mertcan, version 1.0.0
    private ArrayList<BankAccount> accounts = new ArrayList<>(); // ArrayList to hold the bank accounts
    private BankAccount currentAccount = null; // Currently logged-in account ('null' if no-one is logged in)

    /**
     * Constructs a new Bank instance and initializes the accounts list.
     * <p>
     * Week 2 implementation by Mertcan, version 1.0.0.
     * </p>
     */
    public Bank() {
        Debug.trace("Bank::<constructor>");

        // Initialize the accounts list as a new empty ArrayList
        accounts = new ArrayList<>();
    }

    /*
    // a method to create new BankAccounts - this is known as a 'factory method' and is a more
    // flexible way to do it than just using the 'new' keyword directly.
    public BankAccount makeBankAccount(int accNumber, int accPasswd, int balance) {
        return new BankAccount(accNumber, accPasswd, balance);
    }
    */

    /**
     * Checks if an account with the given account number exists.
     * <p>
     * Bora Week 5 version 3.0.4: Added to support unique account number generation
     * </p>
     * 
     * @param accNumber The account number to check
     * @return true if an account with the given number exists, false otherwise
     */
    public boolean accountExists(String accNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccNumber().equals(accNumber)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a bank account to the bank's account repository.
     * <p>
     * This method checks if an account with the same account number already exists.
     * If not, it adds the account to the bank's collection and increments the account counter.
     * </p>
     * <p>
     * Bora Week 5 version 3.0.4: Added to support account creation
     * </p>
     * 
     * @param account The bank account to add
     * @return true if the account was added successfully, false otherwise
     */
    public boolean addBankAccount(BankAccount account) {
        if (accountExists(account.getAccNumber())) {
            return false;
        }
        if (numAccounts < maxAccounts) {
            accounts.add(account);
            numAccounts++;
            Debug.trace("Bank::addBankAccount: Added account " + account.getAccNumber());
            return true;
        } else {
            Debug.trace("Bank::addBankAccount: can't add bank account - too many accounts");
            return false;
        }
    }

    /**
     * Attempts to log in to a bank account using the provided account number and password.
     * <p>
     * The method searches the list of accounts for a match. If a matching account is found,
     * it is set as the current account and {@code true} is returned. Otherwise, the method
     * resets the current account and returns {@code false}.
     * </p>
     * <p>
     * Week 2 implementation by Mertcan, version 1.0.0.
     * </p>
     * <p>
     * Week 5 - Bora - Version 3.0.1: Modified parameter types from int to String to support leading zeros.
     * </p>
     *
     * @param accNumber the account number to log in.
     * @param accPasswd the account password.
     * @return {@code true} if login is successful; {@code false} otherwise.
     */
    public boolean login(String accNumber, String accPasswd) {
        Debug.trace("Bank::login: accNumber = " + accNumber);
        logout(); // logout of any previous account

        // Iterate through all the bank accounts in the accounts list
        for (BankAccount account : accounts) {
            /**
             * Check if the account number and password match the provided ones.
             * <p>
             * Week 5 - Bora - Version 3.0.1: Changed from int comparison to String.equals() 
             * to support leading zeros.
             * </p>
             */
            if (account.getAccNumber().equals(accNumber) && account.getAccPasswd().equals(accPasswd)) {
                // If a match is found, set the current account to the matched account
                currentAccount = account;
                // Log a message indicating the login was successful
                Debug.trace("Bank::login: Login successful for account number: " + accNumber);
                // Return true to indicate that the login was successful
                return true;
            }
        }

        // not found - return false
        Debug.trace("Bank::login: Login failed for accNumber = " + accNumber);
        currentAccount = null;
        return false;
    }

    /**
     * Logs out the currently logged-in account by resetting the current account to {@code null}.
     */
    public void logout()
    {
        if (loggedIn())
        {
            Debug.trace("Bank::logout: logging out, accNumber = " + currentAccount.accNumber);
            currentAccount = null;
        }
    }

    /**
     * Checks whether there is an account currently logged in.
     *
     * @return {@code true} if an account is logged in; {@code false} otherwise.
     */
    public boolean loggedIn() {
        return currentAccount != null;
    }

    /**
     * Deposits money into the currently logged-in account.
     * <p>
     * This method calls the {@code deposit} method on the {@code BankAccount} object.
     * Gur Task Week 4 version 2.0.1.
     * </p>
     *
     * @param amount the amount to deposit.
     * @return {@code true} if the deposit was successful; {@code false} otherwise.
     */
    public boolean deposit(int amount)
    {
        if (loggedIn()) {
            return currentAccount.deposit(amount);
        } else {
            return false;
        }
    }

    /**
     * Withdraws money from the currently logged-in account.
     * <p>
     * This method calls the {@code withdraw} method on the {@code BankAccount} object.
     * Gur Task Week 4 version 2.0.1.
     * </p>
     *
     * @param amount the amount to withdraw.
     * @return {@code true} if the withdrawal was successful; {@code false} otherwise.
     */
    public boolean withdraw(int amount)
    {
        if (loggedIn()) {
            return currentAccount.withdraw(amount);
        } else {
            return false;
        }
    }

    /**
     * Retrieves the balance of the currently logged-in account.
     * <p>
     * This method calls the {@code getBalance} method on the {@code BankAccount} object.
     * Gur Task Week 4 version 2.0.1.
     * </p>
     *
     * @return the account balance, or -1 if no account is logged in.
     */
    public double getBalance()
    {
        if (loggedIn()) {
            return currentAccount.getBalance();
        } else {
            return -1; // use -1 as an indicator of an error
        }
    }

    /**
     * Retrieves the last status message from the currently logged-in account.
     * <p>
     * Gur Task Week 4 version 2.0.1.
     * </p>
     *
     * @return the last message generated by the account.
     */
    public String getLastMessage()
    {
        return currentAccount.getLastMessage();
    }

    /**
     * Changes the password for the currently logged-in account.
     * <p>
     * This method updates the password of the currently logged-in account if the
     * provided account number matches the current account.
     * </p>
     * <p>
     * Week 5 - Made by Bora - Version 3.0.2: Added change password functionality
     * </p>
     *
     * @param accNumber The account number to verify
     * @param newPassword The new password to set
     * @return {@code true} if the password was changed successfully; {@code false} otherwise
     */
    public boolean changePassword(String accNumber, String newPassword)
    {
        if (loggedIn() && currentAccount.getAccNumber().equals(accNumber))
        {
            currentAccount.setAccPasswd(newPassword);
            Debug.trace("Bank::changePassword: Password changed for account " + accNumber);
            return true;
        }
        Debug.trace("Bank::changePassword: Failed to change password for account " + accNumber);
        return false;
    }

    /**
     * Creates a new account with the specified account type and password.
     * <p>
     * This method delegates to the AccountCreator to create a new account
     * with a randomly generated account number.
     * </p>
     * <p>
     * Bora Week 5 version 3.0.4: Simplified to use AccountCreator service
     * </p>
     *
     * @param accountType The type of account to create (student, gold, platinum)
     * @param password The password for the new account
     * @return The account number of the newly created account, or null if creation failed
     */
    public String createNewAccount(String accountType, String password) {
        return AccountCreator.createAccount(this, accountType, password);
    }
}
