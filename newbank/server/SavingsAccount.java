package newbank.server;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SavingsAccount extends Account {

    public SavingsAccount(String accountName, double balance){
        super(accountName,"Savings", balance);
    }

    public void performWithdraw(double amount){
        withdraw(amount);
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
        return "Savings Account: " + getBalance();
    }

    @Override
    public Account loadCustomerAccount(String userId, String accountName, String accountType) {
        return null;
    }

    public static SavingsAccount loadCustomerAccount(String userId, String accountName) {
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
			
            return new SavingsAccount(loadedAccountName, loadedBalance);

        } else {
            System.out.println("Customer data not found. Creating a new customer.");
            SavingsAccount account = new SavingsAccount(accountName,0.0); // Create a new customer object
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
