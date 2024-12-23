package com.atm.model;

import java.util.HashMap;
import java.util.Map;

public class Customer extends User{

    private double balance;
    private Map<String, Double> debtsOwedTo;
    private Map<String, Double> debtsOwedBy;

    public Customer(String name) {
        super(name);
        this.balance = 0.0;
        debtsOwedTo = new HashMap<>();
        debtsOwedBy = new HashMap<>();
    }

    public void deposit(double amount) {
        balance += amount;
        this.debtsOwedTo = new HashMap<>();
        this.debtsOwedBy = new HashMap<>();
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void addDebt(String creditor, double amount) {
        debtsOwedTo.put(creditor, debtsOwedTo.getOrDefault(creditor, 0.0) + amount);
    }

    public void settleDebt(String debtor, double amount) {
        debtsOwedBy.put(debtor, debtsOwedBy.getOrDefault(debtor, 0.0) - amount);
        if (debtsOwedBy.get(debtor) <= 0.0) {
            debtsOwedBy.remove(debtor);
        }
    }

    public void receiveDebt(String debtor, double amount) {
        debtsOwedBy.put(debtor, debtsOwedBy.getOrDefault(debtor, 0.0) + amount);
    }

    public void repayDebt(String creditor, double amount) {
        debtsOwedTo.put(creditor, debtsOwedTo.get(creditor) - amount);
        if (debtsOwedTo.get(creditor) <= 0.0) {
            debtsOwedTo.remove(creditor);
        }
    }

    public double getBalance() {
        return balance;
    }

    public Map<String, Double> getDebtsOwedTo() {
        return debtsOwedTo;
    }

    public Map<String, Double> getDebtsOwedBy() {
        return debtsOwedBy;
    }
}
