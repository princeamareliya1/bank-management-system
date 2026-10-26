package com.bank.service;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.model.CurrentAccount;
import com.bank.model.SavingsAccount;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BankServiceImpl implements BankService {
    private final Map<String, Account> accounts = new HashMap<>();

    @Override
    public void createSavingsAccount(String accountNumber, String holderName, double initialDeposit, double interestRate) {
        if (accounts.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account with number " + accountNumber + " already exists.");
        }
        SavingsAccount account = new SavingsAccount(accountNumber, holderName, initialDeposit, interestRate);
        accounts.put(accountNumber, account);
    }

    @Override
    public void createCurrentAccount(String accountNumber, String holderName, double initialDeposit, double overdraftLimit) {
        if (accounts.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account with number " + accountNumber + " already exists.");
        }
        CurrentAccount account = new CurrentAccount(accountNumber, holderName, initialDeposit, overdraftLimit);
        accounts.put(accountNumber, account);
    }

    @Override
    public void deposit(String accountNumber, double amount) throws AccountNotFoundException {
        Account account = getAccount(accountNumber);
        account.deposit(amount);
    }

    @Override
    public void withdraw(String accountNumber, double amount)
            throws AccountNotFoundException, InsufficientFundsException {
        Account account = getAccount(accountNumber);
        account.withdraw(amount);
    }

    @Override
    public void transfer(String fromAccount, String toAccount, double amount)
            throws AccountNotFoundException, InsufficientFundsException {
        if (fromAccount.equalsIgnoreCase(toAccount)) {
            throw new IllegalArgumentException("Sender and receiver accounts cannot be the same.");
        }
        Account sender = getAccount(fromAccount);
        Account receiver = getAccount(toAccount);

        // Perform atomic-like transfer logic
        sender.withdraw(amount);
        try {
            receiver.deposit(amount);
        } catch (Exception e) {
            // Rollback in case of unexpected failure
            sender.deposit(amount);
            throw e;
        }
    }

    @Override
    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account #" + accountNumber + " not found in the banking system.");
        }
        return account;
    }

    @Override
    public Collection<Account> getAllAccounts() {
        return Collections.unmodifiableCollection(accounts.values());
    }
}
