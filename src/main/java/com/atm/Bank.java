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
     * Adds a new bank account to the bank.
     * <p>
     * The method returns {@code true} if the account is added successfully, or {@code false} if the bank is full.
     * </p>
     *
     * @param account the {@link BankAccount} to add.
     * @return {@code true} if the account was added; {@code false} otherwise.
     */
    public boolean addBankAccount(BankAccount account) {
        if (numAccounts < maxAccounts) {
            accounts.add(account);
            numAccounts++;
            Debug.trace("Bank::addBankAccount: added " +
                    account.accNumber + " " + account.accPasswd + " £" + account.balance);
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
}
