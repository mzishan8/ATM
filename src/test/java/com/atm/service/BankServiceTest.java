package com.atm.service;

import com.atm.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankServiceTest {

    private AuthenticationService authService;
    private BankService bankService;
    private Customer customer;

    @BeforeEach
    void setUp() {
        authService = mock(AuthenticationService.class);
        customer = mock(Customer.class);
        bankService = new BankService(authService);
    }

    @Test
    void shouldNotDepositIfNoCustomerIsLoggedI() {
        when(authService.getLoggedInCustomer()).thenReturn(null);

        bankService.deposit(100.0);

        verify(authService, times(1)).getLoggedInCustomer();
        verifyNoInteractions(customer);
    }

    @Test
    void shouldDepositGivenAmount() {

        when(authService.getLoggedInCustomer()).thenReturn(customer);
        when(customer.getBalance()).thenReturn(200.0);

        bankService.deposit(100.0);

        verify(authService, times(1)).getLoggedInCustomer();
        verify(customer, times(1)).deposit(100.0);
        verify(customer, times(1)).getBalance();
    }
}