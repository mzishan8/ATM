package com.atm.service;

import com.atm.model.Customer;
import com.atm.repository.CustomerRepository;

public class AuthenticationService {
    private final CustomerRepository customerRepository;
    private Customer loggedInCustomer;

    public AuthenticationService(CustomerRepository repository) {
        this.customerRepository = repository;
    }

    public Customer login(String name) {
        loggedInCustomer = customerRepository.findOrCreate(name);
        System.out.println("Hello, " + name + "!");
        return loggedInCustomer;
    }

    public void logout() {
        if (loggedInCustomer == null) {
            System.out.println("Error: No user is currently logged in.");
            return;
        }
        System.out.println("Goodbye, " + loggedInCustomer.getName() + "!");
        loggedInCustomer = null;
    }

    public Customer getLoggedInCustomer() {
        return loggedInCustomer;
    }
}
