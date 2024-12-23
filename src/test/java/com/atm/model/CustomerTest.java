package com.atm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("John");
    }

    @Test
    void shouldBeAbleDepositSuccessful() {
        customer.deposit(100.0);

        assertEquals(100.0, customer.getBalance());
    }

    @Test
    void shouldBeAbleToDepositMultipleDeposits() {
        customer.deposit(50.0);
        customer.deposit(150.0);

        assertEquals(200.0, customer.getBalance());
    }

    @Test
    void shouldGetBalance() {
        customer.deposit(75.0);

        assertEquals(75.0, customer.getBalance());
    }

}