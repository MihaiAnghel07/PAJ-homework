package com.luxoft.bankapp.domain;

import com.luxoft.bankapp.exceptions.NotEnoughFundsException;

import java.util.Objects;

public abstract class AbstractAccount implements Account {

    private int id;
    protected double balance;

    public AbstractAccount(int id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    @Override
    public void deposit(final double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot deposit a negative amount");
        }
        this.balance += amount;
    }

    @Override
    public void withdraw(final double amount) throws NotEnoughFundsException {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot withdraw a negative amount");
        }

        if (amount > maximumAmountToWithdraw()) {
            throw new NotEnoughFundsException(id, balance, amount, "Requested amount exceeds the maximum amount to withdraw");
        }

        this.balance -= amount;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractAccount that = (AbstractAccount) o;
        return id == that.id && Double.compare(balance, that.balance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance);
    }

    @Override
    public int compareTo(Account o) {
        Double b1 = new Double(this.balance);
        Double b2 = new Double(o.getBalance());
        return b1.compareTo(b2);
    }
}
