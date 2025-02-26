package com.atm;

/**
 * Represents a Platinum bank account with specific constraints and behaviors.
 * <p>
 * This class is a subclass of {@link BankAccount} and provides customized account logic for Platinum accounts.
 * For Platinum accounts:
 * <ul>
 *   <li>The withdrawal limit is set to £3000 per transaction.</li>
 *   <li>The deposit limit is set to £2000 per transaction. (Note: Although the comment mentions £3000, the implementation returns £2000.)</li>
 *   <li>The overdraft limit is set to -£1500.</li>
 *   <li>A commission fee of £0.7 is charged on each transaction.</li>
 * </ul>
 * </p>
 * <p>
 * <strong>Modification History:</strong><br>
 * Gur modified the entire account hierarchy in Task Week 4 (version 1.0.1), making the {@code BankAccount} class abstract,
 * setting up inheritance for various account types (including {@code PlatinumAccount}), and implementing different account logics.
 * </p>
 */
public class PlatinumAccount extends BankAccount {

    /**
     * Constructs a PlatinumAccount with the specified account number, password, and initial balance.
     *
     * @param accNumber the unique account number.
     * @param accPasswd the account password.
     * @param balance   the initial balance of the account.
     */
    public PlatinumAccount(int accNumber, int accPasswd, double balance) {
        super(accNumber, accPasswd, balance);
    }

    /**
     * Returns the maximum allowed withdrawal amount for a PlatinumAccount.
     *
     * @return the withdrawal limit of £3000.
     */
    @Override
    protected int getWithdrawalLimit() {
        return 3000;
    }

    /**
     * Returns the maximum allowed deposit amount for a PlatinumAccount.
     *
     * @return the deposit limit of £2000.
     */
    @Override
    protected int getDepositLimit() {
        return 2000;
    }

    /**
     * Returns the overdraft limit for a PlatinumAccount.
     * <p>
     * Platinum accounts are permitted an overdraft down to -£1500.
     * </p>
     *
     * @return the overdraft limit of -£1500.
     */
    @Override
    protected int getOverdraftLimit() {
        return -1500;
    }

    /**
     * Returns the commission fee charged per transaction for a PlatinumAccount.
     *
     * @return the commission fee of £0.7.
     */
    @Override
    protected double getCommission() {
        return 0.7;
    }
}
