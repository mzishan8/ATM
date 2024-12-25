package com.atm;

import com.atm.repository.CustomerRepository;
import com.atm.service.AuthenticationService;
import com.atm.service.BankService;

import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        CustomerRepository customerRepository = new CustomerRepository();
        AuthenticationService authenticationService = new AuthenticationService(customerRepository);
        BankService bankService = new BankService(authenticationService,customerRepository);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            String command = parts[0];

            switch (command) {
                case "login":
                    authenticationService.login(parts[1]);
                    bankService.printAccountSummary(authenticationService.getLoggedInCustomer());
                    break;
                case "deposit":
                    bankService.deposit(Double.parseDouble(parts[1]));
                    break;
                case "transfer":
                    bankService.transfer(parts[1], Double.parseDouble(parts[2]));
                    break;
                case "logout":
                    authenticationService.logout();
                    break;
                case "exit":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Unknown command.");
            }
        }
    }
}
