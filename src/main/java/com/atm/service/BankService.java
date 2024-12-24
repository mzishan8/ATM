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
        for (Map.Entry<String, Double> entry : customer.getDebtsOwedTo().entrySet()) {
            String creditor = entry.getKey();
            double owedAmount = entry.getValue();
            Customer creditorCustomer = customerRepository.find(creditor);
            if (amount >= owedAmount) {
                customer.repayDebt(creditor, owedAmount);
                creditorCustomer.deposit(owedAmount);
                creditorCustomer.settleDebt(customer.getName(), owedAmount);
                amount -= owedAmount;
                System.out.println("Transferred $" + owedAmount + " to " + creditor);
            } else {
                customer.repayDebt(creditor, amount);
                creditorCustomer.deposit(amount);
                creditorCustomer.settleDebt(customer.getName(), amount);
                System.out.println("Transferred $" + amount + " to " + creditor);
                amount = 0;
                break;
            }
        }
        customer.deposit(amount);
        printAvailableBalance();
        customer.getDebtsOwedTo().forEach((customerName, owedAmount) -> System.out.println("Owed $" + owedAmount + " to " + customerName));
    }

    public void transfer(String targetName, double amount) {
        Customer source = authService.getLoggedInCustomer();
        if (source == null) {
            System.out.println("Error: No user logged in.");
            return;
        }

        Customer target = customerRepository.findOrCreate(targetName);
        double owedFromTarget = source.getDebtsOwedBy().getOrDefault(targetName, 0.0);
        if (owedFromTarget >= amount){
            target.repayDebt(source.getName(), amount);
            source.settleDebt(targetName, amount);
            printAvailableBalance();
            printDeptOwedFrom();
            return;
        }
        if (owedFromTarget!= 0 && owedFromTarget < amount){
            target.repayDebt(source.getName(), owedFromTarget);
            source.settleDebt(targetName, owedFromTarget);
            amount -= owedFromTarget;
        }
        if (source.getBalance() >= amount) {
            source.withdraw(amount);
            target.deposit(amount);
            System.out.println("Transferred $" + amount + " to " + targetName);
            printAvailableBalance();
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

    public void printAvailableBalance(){
        Customer customer = authService.getLoggedInCustomer();
        System.out.println("Your balance is $" + customer.getBalance());
    }

    public void printDeptOwedFrom(){
        Customer customer = authService.getLoggedInCustomer();
        customer.getDebtsOwedBy().forEach((key, value) -> System.out.println("Owed $" + value + " from " + key));
    }

    public void printDeptOwedTo(){
        Customer  customer = authService.getLoggedInCustomer();
        customer.getDebtsOwedTo().forEach((key, value) -> System.out.println("Owed $" + value + " to " + key));
    }
}
