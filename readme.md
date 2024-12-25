# ATM CLI Application

This is a Command Line Interface (CLI) application simulating an ATM interaction with a retail bank. 

## Features
- **Login**: Create or log in as a customer.
- **Deposit**: Add money to the current customer's account.
- **Withdraw**: Remove money from the account.
- **Transfer**: Send money to another customer's account.
- **Debt Management**: Handles debts if transfers exceed account balance.
- **Transaction Summary**: Displays account balance and debts.

## Requirements
- Java (JDK 17 or later)
- Gradle (Wrapper included)


## Usage
Start the application by executing the `start.sh` script:
```bash
./start.sh
```

### Commands
- `login [name]`: Logs in as the specified customer, creating the customer if they do not exist.
- `deposit [amount]`: Adds the specified amount to the logged-in customer's account.
- `withdraw [amount]`: Withdraws the specified amount from the logged-in customer's account.
- `transfer [target] [amount]`: Transfers the specified amount to the target customer.
- `logout`: Logs out the current customer.

## Note
- All the amounts are handled by double data type.
- Output will be printed on console