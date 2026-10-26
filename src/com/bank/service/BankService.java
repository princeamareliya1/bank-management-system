package com.bank.service;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import java.util.Collection;

public interface BankService {
    void createSavingsAccount(String accountNumber, String holderName, double initialDeposit, double interestRate);

    void createCurrentAccount(String accountNumber, String holderName, double initialDeposit, double overdraftLimit);

    void deposit(String accountNumber, double amount) throws AccountNotFoundException;

    void withdraw(String accountNumber, double amount) throws AccountNotFoundException, InsufficientFundsException;

    void transfer(String fromAccount, String toAccount, double amount)
            throws AccountNotFoundException, InsufficientFundsException;

    Account getAccount(String accountNumber) throws AccountNotFoundException;

    Collection<Account> getAllAccounts();
}
