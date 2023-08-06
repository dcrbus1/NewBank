package newbank.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class CheckingAccount extends Account {

    public CheckingAccount(String accountName, double balance){
        super(accountName, "Checking", balance);
    }

    public void performWithdraw(double amount){
        withdraw(amount);
    }

     public void performWithdraw(double amount,PrintWriter out){
        withdraw(amount,out);
    }
    public void performMove(double amount, Account destinationAccount){
        move(amount, destinationAccount);
    }

    public void performDeposit(double amount){
        deposit(amount);
    }

    public void getSavingsBalance(){
        getBalance();
    }  

    @Override
    public String toString(){
        return "Checking Account: " + getBalance();
    
    }

    @Override
    public Account loadCustomerAccount(String userId, String accountName, String accountType) {
        return null;
    }

    public static CheckingAccount loadCustomerAccount(String userId, String accountName) {
    try {
        String fileName = "customerData/" + userId +accountName + ".txt";
        File file = new File(fileName);

        if (file.exists()) {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
// reading the values in the same order as they are stored
            String loadedUserId = bufferedReader.readLine();
			
			String loadedAccountName = (bufferedReader.readLine());

			String LoadedAccountType = (bufferedReader.readLine());

			double loadedBalance = Double.parseDouble(bufferedReader.readLine());    


            fileReader.close();

            System.out.println("Customer data loaded successfully.");
			
            return new CheckingAccount(loadedAccountName, loadedBalance);

        } else {
            System.out.println("Customer data not found. Creating a new customer.");
            CheckingAccount account = new CheckingAccount(accountName,0.0); // Create a new customer object
            account.storeCustomerAccount(userId);
            // Save the new customer data
            return account;
        }
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
	
}


}
