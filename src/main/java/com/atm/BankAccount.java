package com.atm;

/**
 * Abstract representation of a bank account.
 * <p>
 * This abstract class defines the template methods for deposit and withdrawal operations,
 * along with hook methods for customizing account-specific parameters such as limits and commission fees.
 * Concrete subclasses (e.g., GoldAccount, PlatinumAccount, StudentAccount) inherit from this class
 * and override these hook methods to provide specific behaviors and account logics.
 * </p>
 * <p>
 * <strong>Modification History:</strong><br>
 * Gur modified the entire class in Task Week 4 (version 1.0.1), made it abstract, set up inheritance for other
 * account classes, and implemented different account logics for deposit, withdrawal, limits, and commissions.
 * <br>
 * Bora modified the class in Task Week 5 (version 3.0.1), changed the account number and password to String to preserve leading zeros.
 * <br>
 * Bora modified the class in Task Week 5 (version 3.0.2), added setAccPasswd method to support password changing functionality.
 * </p>
 */
public abstract class BankAccount {
    /** 
     * The account number for the bank account.
     * Week 5 - Bora - Version 3.0.1: Changed type from int to String to preserve leading zeros.
     */
    protected String accNumber;

    /** 
     * The account password for the bank account.
     * Week 5 - Bora - Version 3.0.1: Changed type from int to String to preserve leading zeros.
     */
    protected String accPasswd;
    /** The current balance of the bank account. */
    protected double balance;
    /** The last message generated by account operations, useful for status feedback. */
    protected String lastMessage;

    /**
     * Constructs a bank account with the specified account number, password, and initial balance.
     *
     * @param accNumber the account number
     * @param accPasswd the account password
     * @param balance the initial account balance
     * <p>
     * Week 5 - Bora - Version 3.0.1: Modified parameter types from int to String for leading zero support.
     * </p>
     */
    public BankAccount(String accNumber, String accPasswd, double balance) {
        this.accNumber = accNumber;
        this.accPasswd = accPasswd;
        this.balance = balance;
    }

    /**
     * Attempts to withdraw the specified amount from the account.
     * <p>
     * The method checks for valid withdrawal conditions:
     * <ul>
     *   <li>The amount must be positive and not exceed the withdrawal limit.</li>
     *   <li>The withdrawal must not cause the balance to drop below the overdraft limit.</li>
     * </ul>
     * A commission fee is applied to each withdrawal.
     * </p>
     *
     * @param amount the amount to withdraw.
     * @return {@code true} if the withdrawal was successful, {@code false} otherwise.
     */
    public boolean withdraw(int amount) {
        if (amount <= 0 || amount > getWithdrawalLimit()) {
            lastMessage = "Invalid withdrawal amount. Must be positive and no more than £" + getWithdrawalLimit() + ".";
            Debug.trace(this.getClass().getSimpleName() + "::withdraw: " + lastMessage);
            return false;
        }
        if (balance - amount < getOverdraftLimit()) {
            lastMessage = "Withdrawal would exceed overdraft limit. Current balance: £" + balance;
            Debug.trace(this.getClass().getSimpleName() + "::withdraw: " + lastMessage);
            return false;
        }
        balance -= (amount + getCommission());
        lastMessage = "Withdrawn £" + amount + ". New balance: £" + balance;
        Debug.trace(this.getClass().getSimpleName() + "::withdraw: " + lastMessage);
        return true;
    }

    /**
     * Attempts to deposit the specified amount into the account.
     * <p>
     * The method checks that the deposit amount is positive and does not exceed the deposit limit.
     * A commission fee is deducted from the deposit. If the net deposit after commission is non-positive,
     * the deposit is rejected.
     * </p>
     *
     * @param amount the amount to deposit.
     * @return {@code true} if the deposit was successful, {@code false} otherwise.
     */
    public boolean deposit(int amount) {
        if (amount <= 0 || amount > getDepositLimit()) {
            lastMessage = "Invalid deposit amount. Must be positive and no more than £" + getDepositLimit() + ".";
            Debug.trace(this.getClass().getSimpleName() + "::deposit: " + lastMessage);
            return false;
        }
        double netDeposit = amount - getCommission();
        if (netDeposit <= 0) {
            lastMessage = "Deposit amount too low after commission deduction.";
            Debug.trace(this.getClass().getSimpleName() + "::deposit: " + lastMessage);
            return false;
        }
        balance += netDeposit;
        lastMessage = "Deposited £" + amount + " (Commission: £" + getCommission() + "). New balance: £" + balance;
        Debug.trace(this.getClass().getSimpleName() + "::deposit: " + lastMessage);
        return true;
    }

    /**
     * Returns the maximum allowed withdrawal amount for this account.
     * <p>
     * This is a "hook" method that can be overridden by subclasses to customize the withdrawal limit.
     * </p>
     *
     * @return the withdrawal limit (default is 200).
     */
    protected int getWithdrawalLimit() {
        return 200; // default withdrawal limit
    }

    /**
     * Returns the maximum allowed deposit amount for this account.
     * <p>
     * This is a "hook" method that can be overridden by subclasses to customize the deposit limit.
     * </p>
     *
     * @return the deposit limit (default is 200).
     */
    protected int getDepositLimit() {
        return 200; // default deposit limit
    }

    /**
     * Returns the overdraft limit for this account.
     * <p>
     * This is a "hook" method that can be overridden by subclasses to define how far into negative balance
     * the account is allowed to go.
     * </p>
     *
     * @return the overdraft limit (default is -100).
     */
    protected int getOverdraftLimit() {
        return -100; // default overdraft limit
    }

    /**
     * Returns the commission fee charged per transaction.
     * <p>
     * This is a "hook" method that can be overridden by subclasses to provide specific commission fees.
     * </p>
     *
     * @return the commission fee (default is 0.5).
     */
    protected double getCommission() {
        return 0.5; // default commission fee
    }

    /**
     * Returns the current balance of the account.
     *
     * @return the current balance.
     */
    protected double getBalance() {
        return balance;
    }

    /**
     * Returns the last message generated by an account operation.
     *
     * @return the last status message.
     */
    protected String getLastMessage() {
        return lastMessage;
    }

    /**
     * Returns the account number.
     *
     * @return the account number.
     * <p>
     * Week 5 - Bora - Version 3.0.1: Changed return type from int to String to support leading zeros.
     * </p>
     */
    protected String getAccNumber() {
        return accNumber;
    }

    /**
     * Returns the account password.
     *
     * @return the account password.
     * <p>
     * Week 5 - Bora - Version 3.0.1: Changed return type from int to String to support leading zeros.
     * </p>
     */
    protected String getAccPasswd() {
        return accPasswd;
    }

    /**
     * Sets a new password for this account.
     * <p>
     * This method allows changing the account password for security purposes.
     * </p>
     * <p>
     * Week 5 - Made by Bora - Version 3.0.2: Added change password functionality
     * </p>
     *
     * @param newPassword the new password to set for the account.
     */
    protected void setAccPasswd(String newPassword) {
        this.accPasswd = newPassword;
    }
}
