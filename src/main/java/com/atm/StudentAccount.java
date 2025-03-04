package com.atm;

/**
 * Represents a student bank account with specific constraints and behaviors.
 * <p>
 * This class is a subclass of {@link BankAccount} and provides customized account logic
 * for student accounts. For student accounts:
 * <ul>
 *   <li>The withdrawal limit is set to £150 per transaction.</li>
 *   <li>Student accounts are not allowed to go into overdraft (overdraft limit is 0).</li>
 *   <li>No commission fees are charged for deposits or withdrawals.</li>
 *   <li>The deposit limit is overridden to be £250.</li>
 * </ul>
 * </p>
 * <p>
 * <strong>Modification History:</strong><br>
 * Gur modified the entire account hierarchy in Task Week 4 (version 1.0.1), making the
 * {@code BankAccount} class abstract and setting up inheritance for the various account types,
 * including {@code StudentAccount}, to implement different account logics.
 * <br>
 * Bora modified constructor parameters from int to String in Week 5 (version 3.0.1)
 * </p>
 */
public class StudentAccount extends BankAccount {

    /**
     * Constructs a StudentAccount with the specified account number, password, and initial balance.
     *
     * @param accNumber the unique account number.
     * @param accPasswd the account password.
     * @param balance   the initial balance of the account.
     */
    public StudentAccount(String accNumber, String accPasswd, double balance) {
        super(accNumber, accPasswd, balance);
    }

    /**
     * Returns the maximum allowed withdrawal amount for a StudentAccount.
     *
     * @return the withdrawal limit of £150.
     */
    @Override
    protected int getWithdrawalLimit() {
        return 150;
    }

    /**
     * Returns the overdraft limit for a StudentAccount.
     * <p>
     * Student accounts are not allowed to go into overdraft; therefore, the overdraft limit is 0.
     * </p>
     *
     * @return the overdraft limit of 0.
     */
    @Override
    protected int getOverdraftLimit() {
        return 0;
    }

    /**
     * Returns the commission fee for a StudentAccount.
     * <p>
     * Student accounts do not incur any commission fees on transactions.
     * </p>
     *
     * @return the commission fee of 0.0.
     */
    @Override
    protected double getCommission() {
        return 0.0;
    }

    /**
     * Returns the maximum allowed deposit amount for a StudentAccount.
     *
     * @return the deposit limit of £250.
     */
    @Override
    protected int getDepositLimit() {
        return 250;
    }
}
