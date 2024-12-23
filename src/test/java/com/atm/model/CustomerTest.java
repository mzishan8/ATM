package com.atm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

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
        assertTrue(customer.getDebtsOwedTo().isEmpty());
        assertTrue(customer.getDebtsOwedBy().isEmpty());
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

    @Test
    void shouldBeAbleToWithdrawSufficientBalance() {
        customer.deposit(300.0);
        boolean success = customer.withdraw(150.0);
        assertTrue(success);
        assertEquals(150.0, customer.getBalance());
    }

    @Test
    void shouldBeAbleWithdrawInsufficientBalance() {
        customer.deposit(100.0);
        boolean success = customer.withdraw(200.0);
        assertFalse(success);
        assertEquals(100.0, customer.getBalance());
    }

    @Test
    void shouldBeAbleToAddDebt() {
        customer.addDebt("Alice", 50.0);
        customer.addDebt("Alice", 30.0);
        Map<String, Double> debtsOwedTo = customer.getDebtsOwedTo();
        assertEquals(80.0, debtsOwedTo.get("Alice"));
    }

    @Test
    void shouldBeAbleToSettleDebtPartialSettlement() {
        customer.receiveDebt("Bob", 100.0);
        customer.settleDebt("Bob", 40.0);
        Map<String, Double> debtsOwedBy = customer.getDebtsOwedBy();
        assertEquals(60.0, debtsOwedBy.get("Bob"));
    }

    @Test
    void shouldBeAbleToSettleDebtFullSettlement() {
        customer.receiveDebt("Bob", 50.0);
        customer.settleDebt("Bob", 50.0);
        Map<String, Double> debtsOwedBy = customer.getDebtsOwedBy();
        assertFalse(debtsOwedBy.containsKey("Bob"));
    }

    @Test
    void shouldBeAbleToReceiveDebt() {
        customer.receiveDebt("Alice", 30.0);
        customer.receiveDebt("Alice", 20.0);
        Map<String, Double> debtsOwedBy = customer.getDebtsOwedBy();
        assertEquals(50.0, debtsOwedBy.get("Alice"));
    }

    @Test
    void shouldBeAbleTORepayDebtPartialRepayment() {
        customer.addDebt("Alice", 70.0);
        customer.repayDebt("Alice", 30.0);
        Map<String, Double> debtsOwedTo = customer.getDebtsOwedTo();
        assertEquals(40.0, debtsOwedTo.get("Alice"));
    }

    @Test
    void shouldBeAbleToRepayDebtFullRepayment() {
        customer.addDebt("Alice", 100.0);
        customer.repayDebt("Alice", 100.0);
        Map<String, Double> debtsOwedTo = customer.getDebtsOwedTo();
        assertFalse(debtsOwedTo.containsKey("Alice"));
    }
}