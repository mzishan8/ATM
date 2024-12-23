package com.atm.service;

import com.atm.model.Customer;
import com.atm.repository.CustomerRepository;

import java.util.Map;

public class BankService {
    private AuthenticationService authService;
    private CustomerRepository customerRepository;

    public BankService(AuthenticationService authService, CustomerRepository customerRepository) {
        this.authService = authService;
        this.customerRepository = customerRepository;
    }

    public void deposit(double amount) {
        Customer customer = authService.getLoggedInCustomer();
        if (customer == null) {
            System.out.println("Error: No user logged in.");
            return;
        }

        customer.deposit(amount);
        for (Map.Entry<String, Double> entry : customer.getDebtsOwedTo().entrySet()) {
            String creditor = entry.getKey();
            double owedAmount = entry.getValue();

            if (amount >= owedAmount) {
                customer.repayDebt(creditor, owedAmount);
                customerRepository.find(creditor).settleDebt(customer.getName(), owedAmount);
                amount -= owedAmount;
            } else {
                customer.repayDebt(creditor, amount);
                customerRepository.find(creditor).settleDebt(customer.getName(), amount);
                break;
            }
        }

        System.out.println("Your balance is $" + customer.getBalance());
    }

    public void transfer(String targetName, double amount) {
        Customer source = authService.getLoggedInCustomer();
        if (source == null) {
            System.out.println("Error: No user logged in.");
            return;
        }

        Customer target = customerRepository.findOrCreate(targetName);

        if (source.getBalance() >= amount) {
            source.withdraw(amount);
            target.deposit(amount);
            System.out.println("Transferred $" + amount + " to " + targetName);
        } else {
            double partialAmount = source.getBalance();
            double debtAmount = amount - partialAmount;

            source.withdraw(partialAmount);
            target.deposit(partialAmount);

            source.addDebt(targetName, debtAmount);
            target.receiveDebt(source.getName(), debtAmount);

            System.out.println("Transferred $" + partialAmount + " to " + targetName);
            System.out.println("Your balance is $0");
            System.out.println("Owed $" + debtAmount + " to " + targetName);
        }
    }
}
