package com.bank;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.service.BankService;
import com.bank.service.BankServiceImpl;
import java.util.Collection;
import java.util.Scanner;

public class Main {
    private static final BankService bankService = new BankServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        seedDemoData();
        printBanner();

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("👉 Select an option (1-8): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleCreateSavingsAccount();
                    break;
                case "2":
                    handleCreateCurrentAccount();
                    break;
                case "3":
                    handleDeposit();
                    break;
                case "4":
                    handleWithdrawal();
                    break;
                case "5":
                    handleTransfer();
                    break;
                case "6":
                    handleAccountStatement();
                    break;
                case "7":
                    handleListAllAccounts();
                    break;
                case "8":
                    running = false;
                    System.out.println("\n👋 Thank you for banking with us. Have a great day!\n");
                    break;
                default:
                    System.out.println("❌ Invalid selection! Please enter a number between 1 and 8.\n");
            }
        }
    }

    private static void seedDemoData() {
        try {
            bankService.createSavingsAccount("SB-1001", "Prince Amreliya", 50000.0, 4.5);
            bankService.createCurrentAccount("CA-2001", "Tech Ventures Ltd", 150000.0, 25000.0);
            bankService.createSavingsAccount("SB-1002", "Aarav Sharma", 12500.0, 4.0);
        } catch (Exception ignored) {
        }
    }

    private static void printBanner() {
        System.out.println("==========================================================");
        System.out.println("       🏦  CONSOLE BANK MANAGEMENT SYSTEM  🏦             ");
        System.out.println("          Built with Core Java & OOP Principles           ");
        System.out.println("==========================================================");
    }

    private static void printMenu() {
        System.out.println("\n----------------- MAIN BANKING MENU -----------------");
        System.out.println("  1. 👤 Open New Savings Account");
        System.out.println("  2. 🏢 Open New Current Account");
        System.out.println("  3. 💵 Deposit Money");
        System.out.println("  4. 💸 Withdraw Money");
        System.out.println("  5. 🔁 Transfer Funds (Inter-Account)");
        System.out.println("  6. 📜 View Account Statement & Details");
        System.out.println("  7. 📋 List All Registered Accounts");
        System.out.println("  8. 🚪 Exit Application");
        System.out.println("-----------------------------------------------------");
    }

    private static void handleCreateSavingsAccount() {
        System.out.println("\n--- [OPEN SAVINGS ACCOUNT] ---");
        try {
            System.out.print("Enter Account Number (e.g. SB-1003): ");
            String accNo = scanner.nextLine().trim();

            System.out.print("Enter Account Holder Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter Initial Deposit (Min ₹500): ₹");
            double deposit = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Annual Interest Rate (%): ");
            double rate = Double.parseDouble(scanner.nextLine().trim());

            bankService.createSavingsAccount(accNo, name, deposit, rate);
            System.out.println("✅ Savings Account created successfully for " + name + "!");
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Invalid numeric input!");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void handleCreateCurrentAccount() {
        System.out.println("\n--- [OPEN CURRENT ACCOUNT] ---");
        try {
            System.out.print("Enter Account Number (e.g. CA-2002): ");
            String accNo = scanner.nextLine().trim();

            System.out.print("Enter Company / Holder Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter Initial Deposit: ₹");
            double deposit = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Overdraft Limit: ₹");
            double overdraft = Double.parseDouble(scanner.nextLine().trim());

            bankService.createCurrentAccount(accNo, name, deposit, overdraft);
            System.out.println("✅ Current Account created successfully for " + name + "!");
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Invalid numeric input!");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void handleDeposit() {
        System.out.println("\n--- [DEPOSIT MONEY] ---");
        try {
            System.out.print("Enter Account Number: ");
            String accNo = scanner.nextLine().trim();

            System.out.print("Enter Deposit Amount: ₹");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            bankService.deposit(accNo, amount);
            Account account = bankService.getAccount(accNo);
            System.out.printf("✅ Deposit successful! New Balance: ₹%,.2f%n", account.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Invalid numeric amount!");
        } catch (AccountNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void handleWithdrawal() {
        System.out.println("\n--- [WITHDRAW MONEY] ---");
        try {
            System.out.print("Enter Account Number: ");
            String accNo = scanner.nextLine().trim();

            System.out.print("Enter Withdrawal Amount: ₹");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            bankService.withdraw(accNo, amount);
            Account account = bankService.getAccount(accNo);
            System.out.printf("✅ Withdrawal successful! Remaining Balance: ₹%,.2f%n", account.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Invalid numeric amount!");
        } catch (AccountNotFoundException | InsufficientFundsException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void handleTransfer() {
        System.out.println("\n--- [FUND TRANSFER] ---");
        try {
            System.out.print("Enter Sender Account Number: ");
            String from = scanner.nextLine().trim();

            System.out.print("Enter Receiver Account Number: ");
            String to = scanner.nextLine().trim();

            System.out.print("Enter Transfer Amount: ₹");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            bankService.transfer(from, to, amount);
            Account sender = bankService.getAccount(from);
            System.out.printf("✅ Transfer of ₹%,.2f completed successfully!%n", amount);
            System.out.printf("   Sender (%s) New Balance: ₹%,.2f%n", from, sender.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Invalid numeric amount!");
        } catch (AccountNotFoundException | InsufficientFundsException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void handleAccountStatement() {
        System.out.println("\n--- [ACCOUNT STATEMENT] ---");
        try {
            System.out.print("Enter Account Number: ");
            String accNo = scanner.nextLine().trim();

            Account account = bankService.getAccount(accNo);
            account.printStatement();
        } catch (AccountNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private static void handleListAllAccounts() {
        System.out.println("\n--- [ALL REGISTERED ACCOUNTS] ---");
        Collection<Account> accounts = bankService.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts currently registered.");
            return;
        }
        System.out.printf("%-12s | %-22s | %-25s | %s%n", "Account No", "Holder Name", "Account Type", "Balance");
        System.out.println("-------------------------------------------------------------------------------------");
        for (Account acc : accounts) {
            System.out.printf("%-12s | %-22s | %-25s | ₹%,.2f%n",
                    acc.getAccountNumber(),
                    acc.getAccountHolderName(),
                    acc instanceof com.bank.model.SavingsAccount ? "Savings Account" : "Current Account",
                    acc.getBalance());
        }
        System.out.println("-------------------------------------------------------------------------------------");
    }
}
