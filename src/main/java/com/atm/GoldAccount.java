package com.atm;

/**
 * Represents a Gold bank account with specific constraints and behaviors.
 * <p>
 * This class is a subclass of {@link BankAccount} and provides customized account logic for Gold accounts.
 * For Gold accounts:
 * <ul>
 *   <li>The withdrawal limit is set to £2000 per transaction.</li>
 *   <li>The deposit limit is set to £2000 per transaction.</li>
 *   <li>The overdraft limit is set to -£1000.</li>
 * </ul>
 * </p>
 * <p>
 * <strong>Modification History:</strong><br>
 * Gur modified the entire account hierarchy in Task Week 4 (version 1.0.1), making the {@code BankAccount} class abstract,
 * setting up inheritance for various account types, and implementing different account logics including that of Gold accounts.
 * </p>
 */
public class GoldAccount extends BankAccount {

    /**
     * Constructs a GoldAccount with the specified account number, password, and initial balance.
     *
     * @param accNumber the unique account number.
     * @param accPasswd the account password.
     * @param balance   the initial balance of the account.
     */
    public GoldAccount(int accNumber, int accPasswd, double balance) {
        super(accNumber, accPasswd, balance);
    }

    /**
     * Returns the maximum allowed withdrawal amount for a GoldAccount.
     *
     * @return the withdrawal limit of £2000.
     */
    @Override
    protected int getWithdrawalLimit() {
        return 2000;
    }

    /**
     * Returns the maximum allowed deposit amount for a GoldAccount.
     *
     * @return the deposit limit of £2000.
     */
    @Override
    protected int getDepositLimit() {
        return 2000;
    }

    /**
     * Returns the overdraft limit for a GoldAccount.
     * <p>
     * Gold accounts have an overdraft limit of -£1000.
     * </p>
     *
     * @return the overdraft limit of -£1000.
     */
    @Override
    protected int getOverdraftLimit() {
        return -1000;
    }
}
