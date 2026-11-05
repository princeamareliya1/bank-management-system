package com.bank.model;

import com.bank.model.Account;
import com.bank.model.CurrentAccount;
import com.bank.model.SavingsAccount;
import com.bank.model.Transaction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * File-based persistence for the banking system. Accounts and their complete
 * transaction history are written to a single text file so that data survives
 * application restarts. No third-party libraries are used.
 *
 * <p>Line format (UTF-8):</p>
 * <ul>
 *   <li>Account header: <b>SAVINGS|</b>&lt;accNo&gt;|&lt;holder&gt;|&lt;balance&gt;|&lt;rate&gt;<br>
 *       or <b>CURRENT|</b>&lt;accNo&gt;|&lt;holder&gt;|&lt;balance&gt;|&lt;overdraft&gt;</li>
 *   <li>Transaction line: <b>TX|</b>&lt;txId&gt;|&lt;type&gt;|&lt;amount&gt;|&lt;balanceAfter&gt;</li>
 *   <li>Block separator: <b>###</b></li>
 * </ul>
 */
public class BankPersistence {

    /** Location of the data file, relative to the process working directory. */
    public static final Path DATA_FILE = Paths.get("bank-data.txt");

    /** Reads saved accounts and transaction history from disk. Returns an empty map if the file is missing. */
    public static Map<String, Account> load() throws IOException {
        Map<String, Account> accounts = new LinkedHashMap<>();
        if (!Files.exists(DATA_FILE)) {
            return accounts;
        }

        List<String> lines = Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8);
        Iterator<String> it = lines.iterator();
        while (it.hasNext()) {
            String line = it.next().trim();
            if (line.isEmpty() || line.equals("###")) {
                continue;
            }

            String[] parts = line.split("\\|", -1);
            if (parts.length < 5) {
                throw new IOException("Invalid account header in " + DATA_FILE + ": " + line);
            }

            String type = parts[0];
            String accNo = parts[1];
            String holder = parts[2];
            double balance = parseDouble(parts[3], "account balance");
            Account account;

            if ("SAVINGS".equals(type)) {
                if (parts.length < 5) {
                    throw new IOException("Invalid savings header in " + DATA_FILE + ": " + line);
                }
                double rate = parseDouble(parts[4], "savings interest rate");
                account = new SavingsAccount(accNo, holder, balance, rate);
            } else {
                if (parts.length < 5) {
                    throw new IOException("Invalid current-account header in " + DATA_FILE + ": " + line);
                }
                double overdraft = parseDouble(parts[4], "current overdraft limit");
                account = new CurrentAccount(accNo, holder, balance, overdraft);
            }

            List<Transaction> history = new ArrayList<>();
            while (it.hasNext()) {
                String t = it.next().trim();
                if (t.equals("###")) {
                    break;
                }
                if (t.isEmpty()) {
                    continue;
                }
                String[] tp = t.split("\\|", -1);
                if (tp.length < 5) {
                    throw new IOException("Invalid transaction line in " + DATA_FILE + ": " + t);
                }
                String txId = tp[1];
                String txType = tp[2];
                double amount = parseDouble(tp[3], "transaction amount");
                double balanceAfter = parseDouble(tp[4], "transaction balance after");
                history.add(new Transaction(txId, txType, amount, balanceAfter));
            }

            account.setTransactions(history);
            accounts.put(accNo, account);
        }
        return accounts;
    }

    /** Writes the current accounts and transaction history to disk. */
    public static void save(Map<String, Account> accounts) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Account acc : accounts.values()) {
            if (acc instanceof SavingsAccount) {
                SavingsAccount sa = (SavingsAccount) acc;
                lines.add("SAVINGS|" + acc.getAccountNumber() + "|" + acc.getAccountHolderName()
                        + "|" + acc.getBalance() + "|" + sa.getInterestRate());
            } else {
                CurrentAccount ca = (CurrentAccount) acc;
                lines.add("CURRENT|" + acc.getAccountNumber() + "|" + acc.getAccountHolderName()
                        + "|" + acc.getBalance() + "|" + ca.getOverdraftLimit());
            }

            for (Transaction tx : acc.getTransactions()) {
                if ("ACCOUNT_OPEN".equals(tx.getType())) {
                    continue; // rebuilt by the account constructor on load
                }
                lines.add("TX|" + tx.getTransactionId() + "|" + tx.getType() + "|"
                        + tx.getAmount() + "|" + tx.getBalanceAfter());
            }
            lines.add("###");
        }
        Files.write(DATA_FILE, lines, StandardCharsets.UTF_8);
    }

    /** Parses a number, wrapping any parse failure as an IOException for consistent error handling. */
    private static double parseDouble(String value, String field) throws IOException {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IOException("Invalid " + field + " in data file: " + value, e);
        }
    }
}
