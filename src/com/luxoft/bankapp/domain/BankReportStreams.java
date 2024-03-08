package com.luxoft.bankapp.domain;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BankReportStreams {

    public static int getNumberOfClients(Bank bank) {
        return bank.getClients().size();
    }

    public static int getNumberOfAccounts(Bank bank) {
        return (int) bank.getClients().stream()
                .mapToLong(client -> client.getAccounts().size())
                .sum();
    }

    public static SortedSet<Client> getClientsSorted(Bank bank) {
        return bank.getClients().stream()
                .sorted(Comparator.comparing(Client::getName))
                .collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
    }

    public static double getTotalSumInAccounts(Bank bank) {
        return bank.getClients().stream()
                .flatMap(client -> client.getAccounts().stream())
                .mapToDouble(Account::getBalance)
                .sum();
    }

    public static SortedSet<Account> getAccountsSortedBySum(Bank bank) {
        return bank.getClients().stream()
                .flatMap(client -> client.getAccounts().stream())
                .sorted(Comparator.comparing(Account::getBalance))
                .collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
    }

    public static double getBankCreditSum(Bank bank) {
        return bank.getClients().stream()
                .flatMap(client -> client.getAccounts().stream())
                .filter(account -> account instanceof CheckingAccount)
                .mapToDouble(checkingAccount -> ((CheckingAccount) checkingAccount).getOverdraft())
                .sum();
    }

    public static Map<Client, Collection<Account>> getCustomerAccounts(Bank bank) {
        return bank.getClients().stream()
                .collect(Collectors.toMap(client -> client, Client::getAccounts));
    }

    public static Map<String, List<Client>> getClientsByCity(Bank bank) {
        return bank.getClients().stream()
                .collect(Collectors.groupingBy(Client::getCity));
    }
}
