package com.luxoft.bankapp.tests;

import com.luxoft.bankapp.domain.*;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.service.BankService;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class BankReportStreamsTest {

    private static Bank bank = new Bank();
    private static List<Client> clientList = new ArrayList<>();
    private static List<Account> accountList = new ArrayList<>();

    @BeforeClass
    public static void init() {
        Client client = new Client("Andrei", Gender.MALE, "Oradea");
        client.addAccount(new CheckingAccount(1, 100, 10));
        client.addAccount(new SavingAccount(2, 80));

        Client client2 = new Client("Maria", Gender.FEMALE, "Brasov");
        client2.addAccount(new CheckingAccount(3, 1000, 140));
        client2.addAccount(new SavingAccount(4, 3500));

        Client client3 = new Client("Mircea", Gender.MALE, "Timisoara");
        client3.addAccount(new CheckingAccount(5, 10, 0));
        client3.addAccount(new SavingAccount(6, 223));

        Client client4 = new Client("Darius", Gender.MALE, "Bacau");
        client4.addAccount(new CheckingAccount(7, 450, 50));
        client4.addAccount(new SavingAccount(8, 810));

        Client client5 = new Client("Ioana", Gender.FEMALE, "Galati");
        client5.addAccount(new SavingAccount(9, 4000));

        Client client6 = new Client("Georgiana", Gender.FEMALE, "Constanta");
        client6.addAccount(new CheckingAccount(10, 110, 12));

        Collections.addAll(clientList, client, client2, client3, client4, client5, client6);
        clientList.forEach(clientX -> accountList.addAll(clientX.getAccounts()));

        try {
            BankService.addClient(bank, client);
            BankService.addClient(bank, client2);
            BankService.addClient(bank, client3);
            BankService.addClient(bank, client4);
            BankService.addClient(bank, client5);
            BankService.addClient(bank, client6);
        } catch (ClientExistsException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public static void clean() {
        clientList.clear();
        clientList = null;

        accountList.clear();
        accountList = null;

        bank = null;
    }

    @Test
    public void getNumberOfClientsTest() {
        int expectedResult = 6;
        int actualResult = BankReportStreams.getNumberOfClients(bank);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getNumberOfAccountsTest() {
        int expectedResult = 10;
        int actualResult = BankReportStreams.getNumberOfAccounts(bank);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getClientsSortedTest() {
        SortedSet<Client> expectedClientSortedSet = new TreeSet<>(clientList);
        SortedSet<Client> actualClientSortedSet = BankReportStreams.getClientsSorted(bank);

        Assert.assertEquals(expectedClientSortedSet, actualClientSortedSet);
    }

    @Test
    public void getTotalSumInAccountsTest() {
        double actualSum = BankReportStreams.getTotalSumInAccounts(bank);
        AtomicReference<Double> expectedSum = new AtomicReference<>((double) 0);
        accountList.forEach(account -> expectedSum.updateAndGet(balance -> (balance + account.getBalance())));

        Assert.assertEquals(expectedSum.get(), actualSum, 0);
    }

    @Test
    public void getAccountsSortedBySumTest() {
        SortedSet<Account> expectedAccountsSortedSet = new TreeSet<>(accountList);
        SortedSet<Account> actualAccountsSortedSet = BankReportStreams.getAccountsSortedBySum(bank);

        Assert.assertEquals(expectedAccountsSortedSet, actualAccountsSortedSet);
    }

    @Test
    public void getBankCreditSumTest() {
        double actualBankCreditSum = BankReportStreams.getBankCreditSum(bank);
        AtomicReference<Double> expectedBankCreditSum = new AtomicReference<>((double) 0);
        accountList.forEach(account -> {
            if (account instanceof CheckingAccount) {
                expectedBankCreditSum.updateAndGet(balance -> (balance + ((CheckingAccount) account).getOverdraft()));
            }
        });

        Assert.assertEquals(expectedBankCreditSum.get(), actualBankCreditSum, 0);
    }

    @Test
    public void getCustomerAccountsTest() {
        Map<Client, Collection<Account>> actualCustomerAccountMap = BankReportStreams.getCustomerAccounts(bank);
        Map<Client, Collection<Account>> expectedCustomerAccountMap = new HashMap<>();
        clientList.forEach(client -> expectedCustomerAccountMap.put(client, client.getAccounts()));

        Assert.assertEquals(expectedCustomerAccountMap, actualCustomerAccountMap);
    }

    @Test
    public void getClientsByCityTest() {
        Map<String, List<Client>> actualClientsByCityMap = BankReportStreams.getClientsByCity(bank);
        Map<String, List<Client>> expectedClientsByCityMap = new HashMap<>();

        clientList.forEach(client -> {
            List<Client> clientList;
            if (expectedClientsByCityMap.containsKey(client.getCity())) {
                clientList = expectedClientsByCityMap.get(client.getCity());

            } else {
                clientList = new ArrayList<>();
            }

            clientList.add(client);
            expectedClientsByCityMap.put(client.getCity(), clientList);
        });

        Assert.assertEquals(expectedClientsByCityMap, actualClientsByCityMap);
    }
}
