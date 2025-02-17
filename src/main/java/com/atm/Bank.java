package com.atm;

import java.util.ArrayList;

// Mertcan Task 2 Update 2.0.0

// Bank class - simple implementation of a bank, with a list of bank accounts, an
// a current account that we are logged in to.

// This class contains one method ('login') which you need to complete as part of
// the lab exercise to make the basic ATM work. Tutors can help you get this part
// working in lab sessions.

// If you choose the ATM for your project, you should make other modifications to
// the system yourself, based on similar examples we will cover in lectures and labs.
public class Bank
{
    // Instance variables containing the bank information
    int maxAccounts = 10;       // maximum number of accounts the bank can hold
    int numAccounts = 0;        // the number of accounts currently in the bank

    
    // Week 2 implementation
    // Implemented by Mertcan, version 1.0.0
    private ArrayList<BankAccount> accounts = new ArrayList<>(); // ArrayList to hold the bank accounts
    private BankAccount currentAccount = null; // Currently logged-in account ('null' if no-one is logged in)
   

    // Constructor method - this provides a couple of example bank accounts to work with
    public Bank() {
        Debug.trace( "Bank::<constructor>");
        
        // Week 2 implementation
        // Implemented by Mertcan, version 1.0.0
        // Initialize the accounts list as a new empty ArrayList
        accounts = new ArrayList<>();
    }

    // a method to create new BankAccounts - this is known as a 'factory method' and is a more
    // flexible way to do it than just using the 'new' keyword directly.
    public BankAccount makeBankAccount(int accNumber, int accPasswd, int balance) {
        return new BankAccount(accNumber, accPasswd, balance);
    }

    // a method to add a new bank account to the bank - it returns true if it succeeds
    // or false if it fails (because the bank is 'full')
    public boolean addBankAccount(BankAccount account) {
        if (numAccounts < maxAccounts) {
            accounts.add(account);
            numAccounts++ ;
            Debug.trace( "Bank::addBankAccount: added " +
                account.accNumber +" "+ account.accPasswd +" £"+ account.balance);
            return true;
        } else {
            Debug.trace( "Bank::addBankAccount: can't add bank account - too many accounts");
            return false;
        }
    }

    // a variant of addBankAccount which makes the account and adds it all in one go.
    // Using the same name for this method is called 'method overloading' - two methods
    // can have the same name if they take different argument combinations
    public boolean addBankAccount(int accNumber, int accPasswd, int balance)
    {
        return addBankAccount(makeBankAccount(accNumber, accPasswd, balance));
    }

    // Check whether the current saved account and password correspond to
    // an actual bank account, and if so login to it (by setting 'account' to it)
    // and return true. Otherwise, reset the account to null and return false
    // YOU NEED TO ADD CODE TO THIS METHOD FOR THE LAB EXERCISE
    public boolean login(int newAccNumber, int newAccPasswd)
    {
        Debug.trace( "Bank::login: accNumber = " + newAccNumber);
        logout(); // logout of any previous account

        // search the array to find a bank account with matching account and password.
        // If you find it, store it in the variable currentAccount and return true.
        // If you don't find it, reset everything and return false

        // YOU NEED TO ADD CODE HERE TO FIND THE RIGHT ACCOUNT IN THE accounts ARRAY,
        // SET THE account VARIABLE AND RETURN true
        

        // Week 2 implementation
        // Implemented by Mertcan, version 1.0.0
        // Iterate through all the bank accounts in the accounts list
        for (BankAccount account : accounts) {
            // Check if the account number and password match the provided ones
            if (account.getAccNumber() == newAccNumber && account.getAccPasswd() == newAccPasswd) {

                // If a match is found, set the current account to the matched account
                currentAccount = account;

                 // Log a message indicating the login was successful
                Debug.trace("Bank::login: Login successful for account number: " + newAccNumber);

                // Return true to indicate that the login was successful
                return true; 
            }
        }

        // not found - return false
        Debug.trace("Bank::login: Login failed for accNumber = " + newAccNumber);
        currentAccount = null;
        return false;

    }

    // Reset the bank to a 'logged out' state
    public void logout()
    {
        if (loggedIn())
        {
            Debug.trace( "Bank::logout: logging out, accNumber = " + currentAccount.accNumber);
            currentAccount  = null;
        }
    }

    // test whether the bank is logged in to an account or not
    public boolean loggedIn() {
        return currentAccount != null;
    }

    // try to deposit money into the account (by calling the deposit method on the
    // BankAccount object)
    public boolean deposit(int amount)
    {
        if (loggedIn()) {
            return currentAccount.deposit(amount);
        } else {
            return false;
        }
    }

    // try to withdraw money into the account (by calling the withdraw method on the
    // BankAccount object)
    public boolean withdraw(int amount)
    {
        if (loggedIn()) {
            return currentAccount.withdraw(amount);
        } else {
            return false;
        }
    }

    // get the account balance (by calling the balance method on the
    // BankAccount object)
    public int getBalance()
    {
        if (loggedIn()) {
            return currentAccount.getBalance();
        } else {
            return -1; // use -1 as an indicator of an error
        }
    }
}
