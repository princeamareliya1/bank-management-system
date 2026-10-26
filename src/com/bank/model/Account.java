package com.bank.model;

import com.bank.exception.InsufficientFundsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class Account {
    protected final String accountNumber;
    protected final String accountHolderName;
    protected double balance;
    protected final List<Transaction> transactions;

    public Account(String accountNumber, String accountHolderName, double initialDeposit) {
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialDeposit;
        this.transactions = new ArrayList<>();

        if (initialDeposit > 0) {
            recordTransaction("ACCOUNT_OPEN", initialDeposit);
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        this.balance += amount;
        recordTransaction("DEPOSIT", amount);
    }

    public abstract void withdraw(double amount) throws InsufficientFundsException;

    public abstract String getAccountType();

    protected void recordTransaction(String type, double amount) {
        String txId = "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        transactions.add(new Transaction(txId, type, amount, this.balance));
    }

    public void displayAccountInfo() {
        System.out.println("--------------------------------------------------");
        System.out.printf(" Account Number : %s%n", accountNumber);
        System.out.printf(" Holder Name    : %s%n", accountHolderName);
        System.out.printf(" Account Type   : %s%n", getAccountType());
        System.out.printf(" Current Balance: ₹%,.2f%n", balance);
        System.out.println("--------------------------------------------------");
    }

    public void printStatement() {
        displayAccountInfo();
        System.out.println("Transaction History:");
        if (transactions.isEmpty()) {
            System.out.println("  No transactions found.");
        } else {
            for (Transaction tx : transactions) {
                System.out.println("  " + tx);
            }
        }
        System.out.println("--------------------------------------------------");
    }
}
