package com.atm.repository;

import com.atm.model.Customer;

import java.util.HashMap;
import java.util.Map;

public class CustomerRepository {
    private final Map<String, Customer> customers = new HashMap<>();

    public Customer findOrCreate(String name) {
        return customers.computeIfAbsent(name, Customer::new);
    }

    public Customer find(String name) {
        return customers.get(name);
    }
}
