package com.atm.service;

import com.atm.model.Customer;
import com.atm.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankServiceTest {

    private AuthenticationService authService;
    private CustomerRepository customerRepository;
    private BankService bankService;
    private Customer loggedInCustomer;
    private Customer targetCustomer;

    @BeforeEach
    void setUp() {
        authService = mock(AuthenticationService.class);
        customerRepository = mock(CustomerRepository.class);
        loggedInCustomer = mock(Customer.class);
        targetCustomer = mock(Customer.class);
        bankService = new BankService(authService, customerRepository);
    }

    @Test
    void shouldNotDepositIfCustomerIsNotLoggedIn() {
        when(authService.getLoggedInCustomer()).thenReturn(null);

        bankService.deposit(100.0);

        verify(authService, times(1)).getLoggedInCustomer();
        verifyNoInteractions(loggedInCustomer);
    }

    @Test
    void shouldDepositGivenAmount() {

        when(authService.getLoggedInCustomer()).thenReturn(loggedInCustomer);
        when(loggedInCustomer.getBalance()).thenReturn(200.0);

        bankService.deposit(100.0);

        verify(authService, times(1)).getLoggedInCustomer();
        verify(loggedInCustomer, times(1)).deposit(100.0);
        verify(loggedInCustomer, times(1)).getBalance();
    }

    @Test
    void shouldNotTransferIfUserNotLoggedIn() {
        when(authService.getLoggedInCustomer()).thenReturn(null);

        bankService.transfer("TargetUser", 100.0);

        verify(authService, times(1)).getLoggedInCustomer();
        verifyNoInteractions(customerRepository, loggedInCustomer, targetCustomer);
    }

    @Test
    void shouldTransferSufficientBalance() {
        String targetName = "TargetUser";
        when(authService.getLoggedInCustomer()).thenReturn(loggedInCustomer);
        when(customerRepository.findOrCreate(targetName)).thenReturn(targetCustomer);
        when(loggedInCustomer.getBalance()).thenReturn(200.0);

        bankService.transfer(targetName, 100.0);

        verify(authService, times(1)).getLoggedInCustomer();
        verify(customerRepository, times(1)).findOrCreate(targetName);
        verify(loggedInCustomer, times(1)).withdraw(100.0);
        verify(targetCustomer, times(1)).deposit(100.0);
    }

    @Test
    void shouldBeAbleToTransferInsufficientBalance() {
        String targetName = "TargetUser";
        when(authService.getLoggedInCustomer()).thenReturn(loggedInCustomer);
        when(customerRepository.findOrCreate(targetName)).thenReturn(targetCustomer);
        when(loggedInCustomer.getBalance()).thenReturn(50.0);
        when(loggedInCustomer.getName()).thenReturn("SourceUser");

        bankService.transfer(targetName, 100.0);

        verify(authService, times(1)).getLoggedInCustomer();
        verify(customerRepository, times(1)).findOrCreate(targetName);
        verify(loggedInCustomer, times(1)).withdraw(50.0);
        verify(targetCustomer, times(1)).deposit(50.0);
        verify(loggedInCustomer, times(1)).addDebt(targetName, 50.0);
        verify(targetCustomer, times(1)).receiveDebt("SourceUser", 50.0);
    }
}