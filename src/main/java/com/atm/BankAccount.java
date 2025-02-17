package com.atm;

// BankAccount class
// This class has instance variables for the account number, password and balance, and methods
// to withdraw, deposit, check balance etc.

// This class contains methods which you need to complete to make the basic ATM work.
// Tutors can help you get this part working in lab sessions.

// If you choose the ATM for your project, you should make other modifications to
// the system yourself, based on similar examples we will cover in lectures and labs.
public class BankAccount
{
    public int accNumber = 0;
    public int accPasswd = 0;
    public int balance = 0;

    // Implemented constructer method Week 2 By Mertcan Kara version 1.0.0
    public BankAccount(int accNumber, int accPasswd, int balance) {
        this.accNumber = accNumber;
        this.accPasswd = accPasswd;
        this.balance = balance;
    }
    


    // withdraw money from the account. Return true if successful, or
    // false if the amount is negative, or less than the amount in the account
    // Withdraw Method implemented by Mertcan week 2 version 1.0.0
    public boolean withdraw( int amount ) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            Debug.trace("BankAccount::withdraw: Withdrawn " + amount);
            return true;
        }
        Debug.trace("BankAccount::withdraw: Insufficient funds or invalid amount");
        return false;
    }

    // deposit the amount of money into the account. Return true if successful,
    // or false if the amount is negative
    // Deposit Method implemented by Mertcan week 2 version 1.0.0
    public boolean deposit(int amount) {
        if (amount > 0) {
            balance += amount;
            Debug.trace("BankAccount::deposit: Deposited " + amount);
            return true;
        }
        Debug.trace("BankAccount::deposit: Invalid deposit amount");
        return false;
    }

    // Preview Balance Method implemented by Mertcan week 2 version 1.0.0
    // Return the current balance in the account
    public int getBalance() {                       
        Debug.trace( "LocalBank::getBalance" );

        // CHANGE CODE HERE TO RETURN THE BALANCE
        return balance; 
    }

    public int getAccNumber() {
        Debug.trace( "LocalBank::getAccNumber" );
        return accNumber;
    }

    public int getAccPasswd() {
        Debug.trace( "LocalBank::getAccPasswd" );
        return accPasswd;
    }
}

