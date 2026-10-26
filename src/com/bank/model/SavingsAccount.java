package com.bank.model;

import com.bank.exception.InsufficientFundsException;

public class SavingsAccount extends Account {
    private static final double MINIMUM_BALANCE = 500.0;
    private final double interestRate; // e.g. 4.0 for 4%

    public SavingsAccount(String accountNumber, String accountHolderName, double initialDeposit, double interestRate) {
        super(accountNumber, accountHolderName, initialDeposit);
        if (initialDeposit < MINIMUM_BALANCE) {
            throw new IllegalArgumentException("Minimum initial deposit for Savings Account is ₹" + MINIMUM_BALANCE);
        }
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public static double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (balance - amount < MINIMUM_BALANCE) {
            throw new InsufficientFundsException(String.format(
                    "Withdrawal denied! Account must maintain a minimum balance of ₹%,.2f. Current balance: ₹%,.2f",
                    MINIMUM_BALANCE, balance));
        }
        this.balance -= amount;
        recordTransaction("WITHDRAWAL", amount);
    }

    public void applyInterest() {
        double interest = (balance * interestRate) / 100.0;
        this.balance += interest;
        recordTransaction("INTEREST_CREDIT", interest);
    }

    @Override
    public String getAccountType() {
        return String.format("Savings Account (Interest: %.2f%%, Min Balance: ₹%,.2f)", interestRate, MINIMUM_BALANCE);
    }
}
