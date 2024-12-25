package com.atm.service;

import com.atm.model.Customer;
import com.atm.repository.CustomerRepository;

import java.util.Map;

public class BankService {
    private final AuthenticationService authService;
    private final CustomerRepository customerRepository;

    public BankService(AuthenticationService authService, CustomerRepository customerRepository) {
        this.authService = authService;
        this.customerRepository = customerRepository;
    }

    public void deposit(double amount) {
        Customer customer = getLoggedInCustomer();
        if (customer == null) return;

        amount = repayDebts(customer, amount); // Repay debts with the deposit amount
        customer.deposit(amount);

        System.out.println("Your balance is $" + customer.getBalance());
        printDebts(customer);
    }

    public void transfer(String targetName, double amount) {
        Customer source = getLoggedInCustomer();
        if (source == null) return;

        Customer target = customerRepository.findOrCreate(targetName);
        amount = settleMutualDebts(source, target, amount); // Settle mutual debts

        if ( amount > 0.0 && source.getBalance() >= amount) {
            source.withdraw(amount);
            target.deposit(amount);
            System.out.println("Transferred $" + amount + " to " + targetName);
        } else if (amount > 0.0){
            double partialAmount = source.getBalance();
            double debtAmount = amount - partialAmount;

            source.withdraw(partialAmount);
            target.deposit(partialAmount);

            source.addDebt(targetName, debtAmount);
            target.receiveDebt(source.getName(), debtAmount);

            System.out.println("Transferred $" + partialAmount + " to " + targetName);
            System.out.println("Your balance is $0");
            System.out.println("Owed $" + debtAmount + " to " + targetName);
            return;
        }

        printAccountSummary(source);
    }

    private double repayDebts(Customer customer, double amount) {
        for (Map.Entry<String, Double> entry : customer.getDebtsOwedTo().entrySet()) {
            String creditor = entry.getKey();
            double owedAmount = entry.getValue();
            Customer creditorCustomer = customerRepository.find(creditor);

            double payment = Math.min(amount, owedAmount);
            customer.repayDebt(creditor, payment);
            creditorCustomer.deposit(payment);
            creditorCustomer.settleDebt(customer.getName(), payment);

            amount -= payment;
            System.out.println("Transferred $" + payment + " to " + creditor);

            if (amount <= 0) break;
        }
        return amount;
    }

    private double settleMutualDebts(Customer source, Customer target, double amount) {
        double owedFromTarget = source.getDebtsOwedBy().getOrDefault(target.getName(), 0.0);

        if (owedFromTarget > 0) {
            double settlementAmount = Math.min(owedFromTarget, amount);
            target.repayDebt(source.getName(), settlementAmount);
            source.settleDebt(target.getName(), settlementAmount);
            amount -= settlementAmount;
        }
        return amount;
    }

    private Customer getLoggedInCustomer() {
        Customer customer = authService.getLoggedInCustomer();
        if (customer == null) {
            System.out.println("Error: No user logged in.");
        }
        return customer;
    }

    private void printDebts(Customer customer) {
        customer.getDebtsOwedTo().forEach((creditor, amount) ->
                System.out.println("Owed $" + amount + " to " + creditor)
        );
        customer.getDebtsOwedBy().forEach((debtor, amount) ->
                System.out.println("Owed $" + amount + " from " + debtor)
        );
    }

    public void printAccountSummary(Customer source){
        System.out.println("Your balance is $" + source.getBalance());
        printDebts(source);
    }
}
