package com.petreca;

import com.petreca.exceptions.*;
import com.petreca.repository.AccountRepository;
import com.petreca.repository.InvestmentRepository;

import java.util.Arrays;
import java.util.Scanner;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();

    public static void main(String[] args) {
        System.out.println("Hello! Welcome to Petreca's Bank!");
        while (true) {
            System.out.println("Select one of the options:");
            System.out.println("1. Create new Account");
            System.out.println("2. Create new Investment Type");
            System.out.println("3. Create Investment Wallet");
            System.out.println("4. Desposit");
            System.out.println("5. Withdraw");
            System.out.println("6. Transfer");
            System.out.println("7. Invest");
            System.out.println("8. Withdraw Investment");
            System.out.println("9. Update Investments");
            System.out.println("10. List Accounts");
            System.out.println("11. List Investments");
            System.out.println("12. List Investment Wallets");
            System.out.println("13. Account History");
            System.out.println("0. Exit");
            var option = scanner.nextInt();
            switch (option){
                case 1 -> createAccount();
                case 2 -> createInvestmentAccount();
                case 3 -> createInvestmentWallet();
                case 4 -> deposit();
                case 5 -> withdraw();
                case 6 -> transfer();
                case 7 -> invest();
                case 8 -> withdrawInvestment();
                case 9 -> updateInvestments();
                case 10 -> listAccounts();
                case 11 -> listInvestments();
                case 12 -> listInvestmentWallets();
                case 13 -> accountHistory();
                case 0 -> {
                    System.out.println("Thank you for using Petreca's Bank! Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    private static void createAccount() {
        System.out.println("Enter the PIX keys (comma separated):");
        scanner.nextLine(); // Consume the newline character
        var pix = Arrays.stream(scanner.next().split(",")).toList();
        System.out.println("Enter the initial funds:");
        var initialFunds = scanner.nextLong();
        try {
            var wallet = accountRepository.create(pix, initialFunds);
            System.out.println("Account created successfully: " + wallet);
        } catch (PixInUseException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void createInvestmentAccount() {
        System.out.println("Enter the tax rate for the investment account:");
        var tax = scanner.nextLong();
        System.out.println("Enter the initial funds for the investment account:");
        var initialFunds = scanner.nextLong();
        var investment = investmentRepository.create(tax, initialFunds);
        System.out.println("Investment account created successfully: " + investment);
    }

    private static void deposit() {
        System.out.println("Enter the PIX key for deposit:");
        scanner.nextLine(); // Consume the newline character
        var pix = scanner.nextLine();
        System.out.println("Enter the amount to deposit:");
        var amount = scanner.nextLong();
        try {
            accountRepository.deposit(pix, amount);
            System.out.println("Deposit successful.");
        } catch (AccountNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void withdraw() {
        System.out.println("Enter the PIX key for withdrawal:");
        scanner.nextLine(); // Consume the newline character
        var pix = scanner.nextLine();
        System.out.println("Enter the amount to withdraw:");
        var amount = scanner.nextLong();
        try {
            accountRepository.withdraw(pix, amount);
            System.out.println("Withdrawal successful.");
        } catch (AccountNotFoundException | NotEnoughFundsException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void transfer() {
        System.out.println("Enter the source PIX key:");
        scanner.nextLine(); // Consume the newline character
        var sourcePix = scanner.nextLine();
        System.out.println("Enter the target PIX key:");
        var targetPix = scanner.nextLine();
        System.out.println("Enter the amount to transfer:");
        var amount = scanner.nextLong();
        try {
            accountRepository.transferMoney(sourcePix, targetPix, amount);
            System.out.println("Transfer successful.");
        } catch (AccountNotFoundException | NotEnoughFundsException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void createInvestmentWallet() {
        System.out.println("Enter the PIX key for the account to link to the investment:");
        scanner.nextLine(); // Consume the newline character
        var pix = scanner.nextLine();
        System.out.println("Enter the investment ID:");
        var investmentId = scanner.nextLong();
        try {
            var account = accountRepository.findByPix(pix);
            var investmentWallet = investmentRepository.initInvestment(account, investmentId);
            System.out.println("Investment wallet created successfully: " + investmentWallet);
        } catch (AccountWithInvestmentException | AccountNotFoundException | NotEnoughFundsException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void invest() {
        System.out.println("Enter the PIX key for investment:");
        scanner.nextLine(); // Consume the newline character
        var pix = scanner.nextLine();
        System.out.println("Enter the amount to invest:");
        var amount = scanner.nextLong();
        try {
            investmentRepository.deposit(pix, amount);
            System.out.println("Investment successful.");
        } catch (WalletNotFoundException | AccountNotFoundException | NotEnoughFundsException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void withdrawInvestment() {
        System.out.println("Enter the PIX key for withdrawal from investment:");
        scanner.nextLine(); // Consume the newline character
        var pix = scanner.nextLine();
        System.out.println("Enter the amount to withdraw from investment:");
        var amount = scanner.nextLong();
        try {
            investmentRepository.withdraw(pix, amount);
            System.out.println("Withdrawal from investment successful.");
        } catch (AccountNotFoundException | NotEnoughFundsException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void updateInvestments() {
        investmentRepository.updateAmount();
        System.out.println("Investment amounts updated successfully.");
    }

    public static void listAccounts() {
        var accounts = accountRepository.list();
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            System.out.println("List of Accounts:");
            accounts.forEach(System.out::println);
        }
    }

    public static void listInvestments() {
        var investments = investmentRepository.list();
        if (investments.isEmpty()) {
            System.out.println("No investments found.");
        } else {
            System.out.println("List of Investments:");
            investments.forEach(System.out::println);
        }
    }

    public static void listInvestmentWallets() {
        var wallets = investmentRepository.listWallets();
        if (wallets.isEmpty()) {
            System.out.println("No investment wallets found.");
        } else {
            System.out.println("List of Investment Wallets:");
            wallets.forEach(System.out::println);
        }
    }

    public static void accountHistory() {
        System.out.println("Enter the PIX key to view account history:");
        scanner.nextLine(); // Consume the newline character
        var pix = scanner.nextLine();
        try {
            var sortedHistory = accountRepository.getHistory(pix);
            System.out.println("Account History for " + pix + ":");
            sortedHistory.forEach((k, v) -> {
                System.out.println(k.format(ISO_DATE_TIME));
                System.out.println(v.getFirst().transactionId());
                System.out.println(v.getFirst().description());
                System.out.println("R$" + (v.size() / 100) + "," + (v.size() % 100));
            });
        } catch (AccountNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
