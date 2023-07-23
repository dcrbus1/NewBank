package newbank.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.List;
import java.lang.Character;
import java.util.ArrayList;

/**
 This program is a multithreaded server that listens to client connections from ExampleClient
 */

public class NewBankServer extends Thread {

    private ServerSocket server;

    public NewBankServer(int port) throws IOException {
        server = new ServerSocket(port);
    }

   

    private static void handleChoiceTwo(Scanner scanner) {
        // Login to an account
        System.out.println("Enter the user ID: ");
        String userId = scanner.nextLine();

        System.out.println("Enter the password: ");
        String password = scanner.nextLine();

        // Validate the credentials and retrieve the Customer object
        Customer customer = UserCredentials.validateCredentials(userId, password);
        if (customer != null) {
            // Additional actions with the customer object can be performed here

            while (true) {
                // Display account options
                //New view balance choice added + getbalance
                System.out.println("------------------------------------------------------------------------------------------------------------------------");
                System.out.println("1. Deposit");
                System.out.println("2. Withdraw");
                System.out.println("3. View balance");
                System.out.println("4. Exit");
                System.out.println("Enter your choice: ");

                int accountChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                if (accountChoice == 1) {
                    // Deposit
                    System.out.println("Select the account to make a deposit:");


                    ArrayList<Account> accounts = customer.getAccounts();
                    int accountIndex = 1;
                    for (Account account : accounts) {
                        System.out.println(accountIndex + "." + account);
                        accountIndex ++;
                    }

                    int selectedAccount = scanner.nextInt();
                    scanner.nextLine(); 

                    // Checks if input is valid and makes deposit in specified account
                    if (selectedAccount >= 1 && selectedAccount <= accounts.size()) {
                        System.out.println("Enter the amount to deposit: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();

                        // Gets the selected acconut and performs the deposit
                        Account selectedAcc =accounts.get(selectedAccount - 1);
                        selectedAcc.deposit(amount);

                    // Saves the updated customer data
                    customer.storeCustomerData(userId);
                    customer.printCustomerData();

                    } else {
                        System.out.println("Invalid account selection.");
                    }
                } else if (accountChoice == 2) {
                    // Withdraw
                    System.out.println("Select an account to withdraw:");


                    ArrayList<Account> accounts = customer.getAccounts();
                    int accountIndex = 1;
                    for (Account account : accounts) {
                        System.out.println(accountIndex + "." + account);
                        accountIndex ++;
                    }
                     int selectedAccount = scanner.nextInt();
                    scanner.nextLine(); 

                    // Checks if input is valid and withdraws from specified account
                    if (selectedAccount >= 1 && selectedAccount <= accounts.size()) {
                        System.out.println("Enter the amount to withdraw: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();

                        // Gets the selected acconut and performs withdraw
                        Account selectedAcc =accounts.get(selectedAccount - 1);
                        selectedAcc.withdraw(amount);

                    customer.storeCustomerData(userId);
                    customer.printCustomerData();
                    } else {
                        System.out.println("Invalid account selection.");
                    }
                } else if (accountChoice == 3) {
                    System.out.println("Select an account to view balance:");

                    ArrayList<Account> accounts = customer.getAccounts();
                    int accountIndex = 1;
                    for (Account account : accounts) {
                        System.out.println(accountIndex + "." + account);
                        accountIndex ++;
                    }
                     int selectedAccount = scanner.nextInt();
                    scanner.nextLine(); 

                    // Checks if input is valid and displays balance from specified account
                    if (selectedAccount >= 1 && selectedAccount <= accounts.size()) {
                        // Gets the selected acconut and performs withdraw
                        Account selectedAcc =accounts.get(selectedAccount - 1);
                        System.out.println("Your balance is : " + selectedAcc.getBalance());

                    customer.storeCustomerData(userId);
                    customer.printCustomerData();
                    } else {
                        System.out.println("Invalid account selection.");
                    }


                } else if (accountChoice == 4) {
                    // Exit
                    break;
                } else {
                    System.out.println("Invalid choice.");
                }
            }
        }
    }

    

    public void run() {
        try {
            while (true) {
                Socket socket = server.accept();
                NewBankClientHandler clientHandler = new NewBankClientHandler(socket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

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


    public static void main(String[] args) throws IOException {
        // Starts a new NewBankServer thread on a specified port number
        NewBankServer bankServer = new NewBankServer(14002);
        bankServer.start();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Display the menu
            System.out.println(
                    "------------------------------------------------------------------------------------------------------------------------");
            System.out.println("1. Create an account");
            System.out.println("2. Login to your account");
            System.out.println("3. List all users");
            System.out.println("Enter your choice: ");

            int choice = 0;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                if (choice == 1) {
                    // Create an account
                    System.out.println("Enter the user ID: ");
                    String userId = scanner.nextLine();
                    String password;
                    do {
                        System.out.println("Enter the password: ");
                        password = scanner.nextLine();
                        // Checks that password meets requirements
                        if (password.length() >= 12 && hasLowerCase(password) && hasUpperCase(password) && containsNumber(password) && containsSpecialCharacter(password)) {
                            System.out.println("Password accepted");
                            // Stores credentials
                            UserCredentials.storeCredentials(userId, password);
                        } else {
                            System.out.println("Password must meet the following requirements: ");
                            System.out.println("1. It must be at least 12 characters long");
                            System.out.println("2. It must contain at least one lower case");
                            System.out.println("3. It must contain at least one upper case");
                            System.out.println("4. It must contain at least one number");
                            System.out.println("4. It must contain at least one special character");
                        }
                        
                    } while (password.length() < 12 || !hasLowerCase(password) || !hasUpperCase(password) || !containsNumber(password) || !containsSpecialCharacter(password));
                    System.out.println("Password accepted");

                } else if (choice == 2) {
                    handleChoiceTwo(scanner);

                } else if (choice == 3) {
                    // List all users
                    List<String> users = UserCredentials.getAllUsers();
                    System.out.println("List of all users:");
                    for (String user : users) {
                        System.out.println(user);
                    }
                }

                else {
                    System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid choice.");
                scanner.nextLine(); // Consume invalid input
            }

            System.out.println("Press Enter key to continue...");
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
