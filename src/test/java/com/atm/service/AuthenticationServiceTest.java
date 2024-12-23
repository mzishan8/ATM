package com.atm.service;

import com.atm.model.Customer;
import com.atm.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    private CustomerRepository customerRepository;
    private AuthenticationService authService;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customer = mock(Customer.class);
        authService = new AuthenticationService(customerRepository);
    }

    @Test
    void shouldBeAbleToLoginSuccessful() {
        String customerName = "ABC";
        when(customerRepository.findOrCreate(customerName)).thenReturn(customer);
        when(customer.getName()).thenReturn(customerName);

        Customer result = authService.login(customerName);

        assertNotNull(result);
        assertEquals(customer, result);
        verify(customerRepository, times(1)).findOrCreate(customerName);
    }

    @Test
    void shouldBeAbleToLogoutSuccessful() {
        String customerName = "John";
        when(customerRepository.findOrCreate(customerName)).thenReturn(customer);
        when(customer.getName()).thenReturn(customerName);

        authService.login(customerName);

        authService.logout();

        assertNull(authService.getLoggedInCustomer());
    }

    @Test
    void shouldNotLogoutIfNoUserLoggedIn() {
        authService.logout();

        assertNull(authService.getLoggedInCustomer());
    }

    @Test
    void shouldGetLoggedInCustomer() {
        String customerName = "John";
        when(customerRepository.findOrCreate(customerName)).thenReturn(customer);

        authService.login(customerName);
        Customer loggedIn = authService.getLoggedInCustomer();

        assertEquals(customer, loggedIn);
    }
}