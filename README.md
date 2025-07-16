# 🏦 Petreca's Bank - Java CLI Banking System

![Java](https://img.shields.io/badge/Java-21-blue?style=for-the-badge&logo=openjdk)
![Gradle](https://img.shields.io/badge/Gradle-8.12-blue?style=for-the-badge&logo=gradle)
![Lombok](https://img.shields.io/badge/Lombok-v1.18.38-red?style=for-the-badge&logo=projectlombok)

A simple yet comprehensive command-line interface (CLI) banking application built with Java. This project simulates basic banking operations, including account management, transactions, and a unique investment system.

---

## ✨ Features

This application supports a variety of banking functionalities:

-   **Account Management:**
    -   👤 Create new accounts with multiple PIX keys.
    -   💰 Deposit funds into an account.
    -   💸 Withdraw funds from an account.
    -   ↔️ Transfer money between accounts.
    -   📜 View detailed transaction history for an account.
-   **Investment System:**
    -   📈 Create different types of investments with specific tax/yield rates.
    -   📁 Link an investment type to a user account to create an "Investment Wallet".
    -   💹 Invest money from the main account into the investment wallet.
    -   🏦 Withdraw funds from the investment wallet back to the main account.
    -   🔄 Simulate investment growth by updating all investment wallets based on their yield rate.
-   **Listing & Viewing:**
    -   📄 List all created accounts.
    -   📊 List all available investment types.
    -   💼 List all active investment wallets.

---

## 🧠 Core Concepts

The project is built around a few key modeling concepts:

### The `Money` and `Wallet` Abstraction

Instead of representing funds as a simple numeric type (`double` or `BigDecimal`), this project uses a more granular approach. The balance of an account is determined by a `List<Money>`.

-   **`Money`**: Each `Money` object represents one unit of currency (e.g., one cent). It contains a complete audit history (`MoneyAudit`) of every transaction it has been a part of.
-   **`Wallet`**: An abstract class that holds a `List<Money>`. It provides the basic mechanics for adding and removing funds.
    -   `AccountWallet`: Represents a standard bank account, identified by PIX keys.
    -   `InvestmentWallet`: Represents a user's investment portfolio, linked to an `AccountWallet` and a specific `Investment` type.

This design allows for a detailed and immutable transaction log for every single unit of currency in the system.

---

## 🚀 Getting Started

### Prerequisites

-   JDK 21 or higher
-   Gradle (the project includes a Gradle wrapper `gradlew`)

### Running the Application

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/your-username/java-bank.git
    cd java-bank
    ```

2.  **Build the project using the Gradle wrapper:**
    -   On Windows:
        ```cmd
        .\gradlew.bat build
        ```
    -   On macOS/Linux:
        ```sh
        ./gradlew build
        ```

3.  **Run the application:**
    You can run the application directly through Gradle or your favorite IDE (like IntelliJ IDEA or VS Code).
    -   **Using Gradle:**
        ```sh
        # On Windows
        .\gradlew.bat run

        # On macOS/Linux
        ./gradlew run
        ```
    -   **Using an IDE:**
        -   Open the project as a Gradle project.
        -   Locate the `Main.java` file in `src/main/java/com/petreca/`.
        -   Run the `main` method.

---

## 🏛️ Project Structure

The project is organized into the following packages:

-   `com.petreca`
    -   `Main.java`: The entry point of the application, handling user input and the main menu.
-   `com.petreca.model`
    -   Contains all the core domain models like `Wallet`, `AccountWallet`, `InvestmentWallet`, `Money`, `Investment`, etc.
-   `com.petreca.repository`
    -   Acts as a data access layer (in-memory). Manages the collections of `AccountWallet`s and `Investment`s and contains the business logic for operations.
-   `com.petreca.exceptions`
    -   Custom exception classes for handling specific error scenarios (e.g., `AccountNotFoundException`, `NotEnoughFundsException`).

---

## 💻 Usage Example

Upon running the application, you will be greeted with a menu:

```
Hello! Welcome to Petreca's Bank!
Select one of the options:
1. Create new Account
2. Create new Investment Type
...
0. Exit
> 1
Enter the PIX keys (comma separated):
my-email@test.com,123456789
Enter the initial funds:
10000
Account created successfully: AccountWallet{Service = ACCOUNT; Funds = R$100,0; Pix = [my-email@test.com, 123456789]}
```
 
## 🤝 Contributing & Final Words

Thank you for checking out Petreca's Bank! I had a great time building it and I hope you find it interesting.
I am always open to new ideas, feedback, and contributions from the community. If you have any suggestions for new features, improvements, or bug fixes, please feel free to open an issue or submit a pull request. Let's make this project even better together!
Happy coding! 😄