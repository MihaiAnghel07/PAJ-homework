package com.luxoft.bankapp.main;

import com.luxoft.bankapp.domain.*;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.exceptions.NotEnoughFundsException;
import com.luxoft.bankapp.exceptions.OverdraftLimitExceededException;
import com.luxoft.bankapp.service.BankService;

import java.util.*;
import java.util.stream.Collectors;

public class BankApplication {

    private static Bank bank;

    public static void main(String[] args) {
        bank = new Bank();
        modifyBank();
        printBalance();
        BankService.printMaximumAmountToWithdraw(bank);
        if (Arrays.stream(args).collect(Collectors.toList()).contains("-statistics")) {
            printBankStatistics();
        }
    }

    private static void modifyBank() {
        Client client1 = new Client("John", Gender.MALE, "New York");
        Client client2 = new Client("Bob", Gender.MALE, "Budapest");
        Client client3 = new Client("Mirela", Gender.FEMALE, "Bucharest");

        Account account1 = new SavingAccount(1, 100);
        Account account2 = new CheckingAccount(2, 100, 20);
        Account account3 = new SavingAccount(3, 400);
        Account account4 = new CheckingAccount(4, 40, 5);
        Account account5 = new SavingAccount(5, 60);
        Account account6 = new CheckingAccount(6, 210, 45);
        client1.addAccount(account1);
        client1.addAccount(account2);
        client2.addAccount(account3);
        client2.addAccount(account4);
        client3.addAccount(account5);
        client3.addAccount(account6);

        try {
            BankService.addClient(bank, client1);
        } catch (ClientExistsException e) {
            System.out.format("Cannot add an already existing client: %s%n", client1.getName());
        }

        account1.deposit(100);
        try {
            account1.withdraw(10);
        } catch (OverdraftLimitExceededException e) {
            System.out.format("Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
        } catch (NotEnoughFundsException e) {
            System.out.format("Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getAmount());
        }

        try {
            account2.withdraw(90);
        } catch (OverdraftLimitExceededException e) {
            System.out.format("Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
        } catch (NotEnoughFundsException e) {
            System.out.format("Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getAmount());
        }

        try {
            account2.withdraw(100);
        } catch (OverdraftLimitExceededException e) {
            System.out.format("Not enough funds for account %d, balance: %.2f, overdraft: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
        } catch (NotEnoughFundsException e) {
            System.out.format("Not enough funds for account %d, balance: %.2f, tried to extract amount: %.2f%n", e.getId(), e.getBalance(), e.getAmount());
        }

        try {
            BankService.addClient(bank, client1);
        } catch (ClientExistsException e) {
            System.out.format("Cannot add an already existing client: %s%n", client1);
        }

        try {
            BankService.addClient(bank, client2);
        } catch (ClientExistsException e) {
            System.out.format("Cannot add an already existing client: %s%n", client2);
        }

        try {
            BankService.addClient(bank, client3);
        } catch (ClientExistsException e) {
            System.out.format("Cannot add an already existing client: %s%n", client3);
        }

    }

    private static void printBalance() {
        System.out.format("%nPrint balance for all clients%n");
        for (Client client : bank.getClients()) {
            System.out.println("Client: " + client);
            for (Account account : client.getAccounts()) {
                System.out.format("Account %d : %.2f%n", account.getId(), account.getBalance());
            }
        }
    }

    private static void printBankStatistics() {
        System.out.println("==================Print bank statistics==================");
        System.out.format("Number of clients: %d%n", BankReport.getNumberOfClients(bank));
        System.out.format("Total number of accounts: %d%n", BankReport.getNumberOfAccounts(bank));
        System.out.println("Clients sorted by name");
        SortedSet<Client> clients = BankReport.getClientsSorted(bank);
        clients.forEach(client -> System.out.println("    - " + client));
        System.out.format("%nTotal sum in accounts: %.2f%n", BankReport.getTotalSumInAccounts(bank));
        System.out.println("Accounts sorted by current balance");
        SortedSet<? extends Account> accounts = BankReport.getAccountsSortedBySum(bank);
        accounts.forEach(account -> System.out.println("    - id: " + account.getId() + "   balance: " + account.getBalance()));
        System.out.format("%nThe total amount of credits granted to the bank clients: %.2f%n", BankReport.getBankCreditSum(bank));
        System.out.println("Customers and their accounts");
        BankReport.getCustomerAccounts(bank).forEach(((client, clientAccounts) ->
                System.out.println("    - " + client + "  - Account(s): " + clientAccounts)));
        System.out.format("%nCustomers by city%n");
        Map<String, List<Client>> cityMap = new TreeMap<>(BankReport.getClientsByCity(bank));
        cityMap.forEach((city, clientList) -> System.out.println("    - " + city + "  - Customer(s): " + clientList));
        System.out.format("%n==========================================================%n");

    }

}
