package com.bank.model;

import com.bank.exception.InsufficientFundsException;

public class CurrentAccount extends Account {
    private final double overdraftLimit;

    public CurrentAccount(String accountNumber, String accountHolderName, double initialDeposit, double overdraftLimit) {
        super(accountNumber, accountHolderName, initialDeposit);
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (balance - amount < -overdraftLimit) {
            throw new InsufficientFundsException(String.format(
                    "Withdrawal denied! Overdraft limit of ₹%,.2f exceeded. Available limit: ₹%,.2f",
                    overdraftLimit, balance + overdraftLimit));
        }
        this.balance -= amount;
        recordTransaction("WITHDRAWAL", amount);
    }

    @Override
    public String getAccountType() {
        return String.format("Current Account (Overdraft Limit: ₹%,.2f)", overdraftLimit);
    }
}
