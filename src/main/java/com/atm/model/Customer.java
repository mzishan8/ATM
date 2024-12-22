package com.atm.model;

public class Customer extends User{

    private double balance;

    public Customer(String name) {
        super(name);
        this.balance = 0.0;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }
}
