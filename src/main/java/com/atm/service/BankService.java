package com.atm.service;

import com.atm.model.Customer;

public class BankService {
    private AuthenticationService authService;

    public BankService(AuthenticationService authService) {
        this.authService = authService;
    }

    public void deposit(double amount) {
        Customer customer = authService.getLoggedInCustomer();
        if (customer == null) {
            System.out.println("Error: No user logged in.");
            return;
        }

        customer.deposit(amount);
        System.out.println("Your balance is $" + customer.getBalance());
    }
}
