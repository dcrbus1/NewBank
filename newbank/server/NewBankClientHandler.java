package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NewBankClientHandler extends Thread{
	
	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	

	    // Function to check if the password has at least one lower case
        private static boolean hasLowerCase(String password) {
            for (char c : password.toCharArray()){
                if (Character.isLowerCase(c)){
                    return true;
                }
            }
            return false;
        }

        // Function to check if the password has at least one upper case
          private static boolean hasUpperCase(String password) {
            for (char c : password.toCharArray()){
                if (Character.isUpperCase(c)){
                    return true;
                }
            }
            return false;
        }

        // Function to check if the password contains at least one number
        private static boolean containsNumber(String password){
            for (char c : password.toCharArray()){
                if (Character.isDigit(c)){
                    return true;
                }
            }
            return false;
        } 

        // Function to check if the password contains at least one special character
                private static boolean containsSpecialCharacter(String password){
            String specialCharacters = "!@#$%^&*()-=_+[]{}|;:,.<>?";
            for (char c : password.toCharArray()){
                if (specialCharacters.contains(String.valueOf(c))){
                    return true;
                }
            }
            return false;
        }

    private Customer userLoginAttempt(Scanner scanner){
         // Login to an account
        out.println("Enter the user ID: ");
        String userId = scanner.nextLine();

        out.println("Enter the password: ");
        String password = scanner.nextLine();

        // Validate the credentials and retrieve the Customer object
        return UserCredentials.validateCredentials(userId, password, out, in);
    }
    private int mainMenu(){

    }
    private void depositAction(Customer customer, Scanner scanner){
        // Deposit
        out.println("Select the account to make a deposit:");
        ArrayList<Account> accounts = customer.getAccounts();
        int accountIndex = 1;
        for (Account account : accounts) {
            out.println(accountIndex + "." + account);
            accountIndex++;
        }

        int selectedAccount = scanner.nextInt();
        scanner.nextLine();
        // Checks if input is valid and makes deposit in specified account
        if (selectedAccount >= 1 && selectedAccount <= accounts.size()) {
            out.println("Enter the amount to deposit: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            // Gets the selected acconut and performs the deposit
            Account selectedAcc = accounts.get(selectedAccount - 1);
            selectedAcc.deposit(amount);
            // Saves the updated customer data
            customer.storeCustomerData(customer.getUserId());
            customer.printCustomerData();
        } else {
            out.println("Invalid account selection.");
        }
    }

    private void withdrawAction(Customer customer, Scanner scanner){
        // Withdraw
        out.println("Select an account to withdraw:");
        ArrayList<Account> accounts = customer.getAccounts();
        int accountIndex = 1;
        for (Account account : accounts) {
            out.println(accountIndex + "." + account);
            accountIndex++;
        }
        int selectedAccount = scanner.nextInt();
        scanner.nextLine();

        // Checks if input is valid and withdraws from specified account
        if (selectedAccount >= 1 && selectedAccount <= accounts.size()) {
            out.println("Enter the amount to withdraw: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            // Gets the selected acconut and performs withdraw
            Account selectedAcc = accounts.get(selectedAccount - 1);
            selectedAcc.withdraw(amount,out);

            customer.storeCustomerData(customer.getUserId());
            customer.printCustomerData();
        } else {
            out.println("Invalid account selection.");
        }
    }

    private void balanceAction(Customer customer, Scanner scanner){
        out.println("Balances:");

        ArrayList<Account> accounts = customer.getAccounts();
        int accountIndex = 1;
        for (Account account : accounts) {
            out.println(accountIndex + "." + account);
            accountIndex++;
        }
        out.println("Press any key to continue");
        scanner.nextLine();
    }
    private void internalMoveAction(Customer customer, Scanner scanner ){
        out.println("Select the account to move money from:");

                    ArrayList<Account> accounts = customer.getAccounts();
                    int accountIndex = 1;
                    for (Account account : accounts) {
                        out.println(accountIndex + "." + account);
                        accountIndex++;
                    }

                    int fromAccountIndex = scanner.nextInt();
                    scanner.nextLine();

                    if (fromAccountIndex >= 1 && fromAccountIndex <= accounts.size()) {
                        out.println("Enter the amount to withdraw and transfer: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();

                        out.println("Select the account to deposit to:");
                        accountIndex = 1;
                        for (Account account : accounts) {
                            out.println(accountIndex + "." + account);
                            accountIndex++;
                        }

                        int toAccountIndex = scanner.nextInt();
                        scanner.nextLine();

                        if (toAccountIndex >= 1 && toAccountIndex <= accounts.size()) {
                            // Perform the transfer between accounts
                            Account fromAccount = accounts.get(fromAccountIndex - 1);
                            Account toAccount = accounts.get(toAccountIndex - 1);
                            fromAccount.withdraw(amount);
                            toAccount.deposit(amount);

                            customer.storeCustomerData(customer.getUserId());
                            customer.printCustomerData();
                        } else {
                            out.println("Invalid account selection for deposit.");
                        }
                    } else {
                        out.println("Invalid account selection for withdraw.");
                    }
    }

    private void moveExternalAction(Customer customer, Scanner scanner){
        
                    // Pay another customer's account
                    out.println("Choose the account from which to pay: ");

                    ArrayList<Account> accounts = customer.getAccounts();
                    int accountIndex = 1;
                    for (Account account : accounts) {
                        out.println(accountIndex + "." + account.getAccountName());
                        accountIndex++;
                    }

                    int fromAccountIndex = scanner.nextInt();
                    scanner.nextLine();

                    if (fromAccountIndex >= 1 && fromAccountIndex <= accounts.size()) {
                        out.println("Enter the amount to pay: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();

                        out.println("Enter the userID of the customer who will receive the payment: ");
                        String receivingUserId = scanner.nextLine();

                        out.println("Enter the account name of the receiver customer to deposit your payment to:");
                        String receivingAccountName = scanner.nextLine();

                        Customer receivingCustomer;
                        receivingCustomer = Customer.loadCustomerData(receivingUserId);
                        Account toAccount = receivingCustomer.getAccountByName(receivingAccountName);
                        if (toAccount == null) {
                            out.println("Error: Invalid receiving account name.");
                            
                        }
                        Account fromAccount = accounts.get(fromAccountIndex - 1);
                        // Add error handling for insufficient funds.
                        if (fromAccount.getBalance() >= amount) {
                            fromAccount.withdraw(amount);
                            toAccount.deposit(amount);
                            // Save both customer's data
                            customer.storeCustomerData(customer.getUserId());
                            receivingCustomer.storeCustomerData(receivingUserId);
                        } else {
                            out.println("Error: Insufficient funds in your account.");
                        }
                    } else {
                        out.println("Invalid account selection for payment.");
                    }

    }
    private void handleChoiceTwo(Scanner scanner, PrintWriter out, BufferedReader in) {
        Customer customer = userLoginAttempt(scanner);
        if (customer != null) {
            // Additional actions with the customer object can be performed here
            String userId=customer.getUserId();
            while (true) {
                // Display account options
                //New view balance choice added + getbalance AP
                //Order changed + move money method added
                out.println("------------------------------------------------------------------------------------------------------------------------");
                out.println("1. Deposit");
                out.println("2. Withdraw");
                out.println("3. View balance");
                out.println("4. Move money between accounts"); // Added choice
                out.println("5. Pay another customer");
                out.println("6. Exit");
                out.println("Enter your choice: ");

                int accountChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                if (accountChoice == 1) {
                    depositAction(customer,scanner);
                } else if (accountChoice == 2) {
                    withdrawAction(customer,scanner);
                } else if (accountChoice == 3) {
                    balanceAction(customer, scanner);
                } else if (accountChoice == 4) {
                    internalMoveAction(customer, scanner);
                } else if (accountChoice == 5) {
                    moveExternalAction(customer,scanner);
                } else if (accountChoice == 6) {

                    //Exit
                    break;

                } else {
                    out.println("Invalid choice.");
                }

            }
        }
        }
	
	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}
	
	public void run() {
		// keep getting requests from the client and processing them
		try {

        while (true) {
            // Display the menu
            out.println(
                    "------------------------------------------------------------------------------------------------------------------------");
            out.println("1. Create an account");
            out.println("2. Login to your account");
            out.println("3. List all users");
            out.println("Enter your choice: ");
		  Scanner scanner = new Scanner(in);

            int choice = 0;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                if (choice == 1) {
                    // Create an account
                    out.println("Enter the user ID: ");
                    String userId = scanner.nextLine();
                    String password;
                    do {
                        out.println("Enter the password: ");
                        password = scanner.nextLine();
                        // Checks that password meets requirements
                        if (password.length() >= 12 && hasLowerCase(password) && hasUpperCase(password) && containsNumber(password) && containsSpecialCharacter(password)) {
                            out.println("Password accepted");
                            // Stores credentials
                            UserCredentials.storeCredentials(userId, password,out, in);
                        } else {
                            out.println("Password must meet the following requirements: ");
                            out.println("1. It must be at least 12 characters long");
                            out.println("2. It must contain at least one lower case");
                            out.println("3. It must contain at least one upper case");
                            out.println("4. It must contain at least one number");
                            out.println("4. It must contain at least one special character");
                        }
                        
                    } while (password.length() < 12 || !hasLowerCase(password) || !hasUpperCase(password) || !containsNumber(password) || !containsSpecialCharacter(password));
                    out.println("Password accepted");

                } else if (choice == 2) {
                    handleChoiceTwo(scanner,out,in);

                } else if (choice == 3) {
                    // List all users
                    List<String> users = UserCredentials.getAllUsers();
                    out.println("List of all users:");
                    for (String user : users) {
                        out.println(user);
                    }
                }

                else {
                    out.println("Invalid choice.");
                }
            } catch (Exception e) {
                out.println("Invalid input. Please enter a valid choice.");
                scanner.nextLine(); // Consume invalid input
            }

            out.println("Press Enter key to continue...");
            try {
                in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

}
