package com.luxoft.bankapp.domain;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public abstract class BankReport {

    public static int getNumberOfClients(Bank bank) {
        return bank.getClients().size();
    }

    public static int getNumberOfAccounts(Bank bank) {
        AtomicInteger nrClients = new AtomicInteger();
        bank.getClients().forEach(client -> nrClients.addAndGet(client.getAccounts().size()));

        return nrClients.get();
    }

    public static SortedSet<Client> getClientsSorted(Bank bank) {
        return new TreeSet<>(bank.getClients());
    }

    public static double getTotalSumInAccounts(Bank bank) {
        AtomicReference<Double> totalSum = new AtomicReference<>((double) 0);

        bank.getClients().forEach(client ->
                client.getAccounts().forEach(account ->
                        totalSum.updateAndGet(balance -> (balance + account.getBalance()))));

        return totalSum.get();
    }

    public static SortedSet<Account> getAccountsSortedBySum(Bank bank) {
        SortedSet<Account> sortedAccounts = new TreeSet<>();
        bank.getClients().forEach(client -> sortedAccounts.addAll(client.getAccounts()));

        return sortedAccounts;
    }

    public static double getBankCreditSum(Bank bank) {
        AtomicReference<Double> bankCreditSum = new AtomicReference<>((double) 0);

        bank.getClients().forEach(client -> client.getAccounts().forEach(account -> {
            if (account instanceof CheckingAccount) {
                bankCreditSum.updateAndGet(balance -> (balance + ((CheckingAccount) account).balance));
            }
        }));

        return bankCreditSum.get();
    }

    public static Map<Client, Collection<Account>> getCustomerAccounts(Bank bank) {
        Map<Client, Collection<Account>> customerAccounts = new HashMap<>();
        bank.getClients().forEach(client -> customerAccounts.put(client, client.getAccounts()));

        return customerAccounts;
    }

    public static Map<String, List<Client>> getClientsByCity(Bank bank) {
        Map<String, List<Client>> clientsByCity = new HashMap<>();

        bank.getClients().forEach(client -> {
            List<Client> clientList;
            if (clientsByCity.containsKey(client.getCity())) {
                clientList = clientsByCity.get(client.getCity());

            } else {
                clientList = new ArrayList<>();
            }

            clientList.add(client);
            clientsByCity.put(client.getCity(), clientList);
        });

        return clientsByCity;
    }

}
