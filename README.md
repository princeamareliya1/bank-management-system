# 🏦 Console Bank Management System

![Java](https://img.shields.io/badge/Java-21+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Paradigm](https://img.shields.io/badge/Paradigm-Object--Oriented-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-success?style=for-the-badge)

A modular, console-based banking application developed in Core Java. The system simulates real-world banking operations—including savings/current account management, fund transfers, transaction auditing, and custom exceptions—engineered to demonstrate fundamental **Object-Oriented Programming (OOP)** principles and clean code architecture.

---

## 🌟 Key Features

* **Multi-Tier Account Support:** Supports both **Savings Accounts** (minimum balance enforcement & interest accrual) and **Current Accounts** (custom overdraft limits).
* **Inter-Account Fund Transfers:** Atomic-style transfer mechanism validating sender solvency before crediting the destination.
* **Detailed Transaction Auditing:** Every transaction (Deposit, Withdrawal, Transfer, Interest) generates an immutable timestamped audit record with a unique transaction ID (`TX-XXXXXXXX`).
* **Robust Exception Handling:** Gracefully handles business rule violations using custom checked exceptions (`InsufficientFundsException`, `AccountNotFoundException`).
* **Interactive CLI Interface:** Intuitive command-line interface with pre-seeded demo accounts for quick testing.

---

## 🏛 Object-Oriented Principles Applied

| OOP Pillar | Implementation in This Project |
| :--- | :--- |
| **Encapsulation** | Sensitive fields (`balance`, `transactions`, `accountNumber`) are declared `private`/`protected` with controlled access via getters, setters, and business methods. |
| **Inheritance** | Base abstract class `Account` encapsulates shared state, extended by specialized `SavingsAccount` and `CurrentAccount` subclasses. |
| **Polymorphism** | Dynamic method overriding for `withdraw()` and `getAccountType()` to apply account-specific business logic at runtime. |
| **Abstraction** | Service layer decoupled via the `BankService` interface, hiding internal storage implementation (`HashMap`) from the consumer (`Main.java`). |

---

## 📁 Project Architecture

```
bank-management-system/
├── src/
│   └── com/
│       └── bank/
│           ├── Main.java                          # CLI entry point & menu runner
│           ├── model/
│           │   ├── Account.java                   # Abstract base account entity
│           │   ├── SavingsAccount.java            # Account with minimum balance & interest
│           │   ├── CurrentAccount.java            # Account with overdraft protection
│           │   └── Transaction.java               # Immutable audit log entry
│           ├── exception/
│           │   ├── InsufficientFundsException.java# Checked exception for balance violations
│           │   └── AccountNotFoundException.java  # Checked exception for missing accounts
│           └── service/
│               ├── BankService.java               # Service abstraction interface
│               └── BankServiceImpl.java           # Business logic & collection manager
├── .gitignore                                     # Clean build artifact filters
└── README.md                                      # Documentation & usage guide
```

---

## 🚀 Getting Started

### Prerequisites
* Java Development Kit (JDK) 11 or higher (`javac` and `java` in your system `PATH`).

### Compilation & Execution

1. **Clone the repository:**
   ```bash
   git clone https://github.com/princeamareliya1/bank-management-system.git
   cd bank-management-system
   ```

2. **Compile the source code:**
   ```bash
   # Compile all Java files into a bin directory
   javac -d bin src/com/bank/*.java src/com/bank/model/*.java src/com/bank/exception/*.java src/com/bank/service/*.java
   ```

3. **Run the application:**
   ```bash
   java -cp bin com.bank.Main
   ```

---

## 💻 Sample Console Walkthrough

```text
==========================================================
       🏦  CONSOLE BANK MANAGEMENT SYSTEM  🏦             
          Built with Core Java & OOP Principles           
==========================================================

----------------- MAIN BANKING MENU -----------------
  1. 👤 Open New Savings Account
  2. 🏢 Open New Current Account
  3. 💵 Deposit Money
  4. 💸 Withdraw Money
  5. 🔁 Transfer Funds (Inter-Account)
  6. 📜 View Account Statement & Details
  7. 📋 List All Registered Accounts
  8. 🚪 Exit Application
-----------------------------------------------------
👉 Select an option (1-8): 6

--- [ACCOUNT STATEMENT] ---
Enter Account Number: SB-1001
--------------------------------------------------
 Account Number : SB-1001
 Holder Name    : Prince Amreliya
 Account Type   : Savings Account (Interest: 4.50%, Min Balance: ₹500.00)
 Current Balance: ₹50,000.00
--------------------------------------------------
Transaction History:
  [2026-10-26 12:30:00] ID: TX-A1B2C3D4 | ACCOUNT_OPEN  | Amount: ₹ 50,000.00 | Balance: ₹ 50,000.00
--------------------------------------------------
```

---

## 👨‍💻 Author

**Prince Amreliya**  
* B.Tech Computer Science & Engineering, Darshan University
* [LinkedIn](https://www.linkedin.com/in/prince-amareliya-848723379) • [GitHub](https://github.com/princeamareliya1)
