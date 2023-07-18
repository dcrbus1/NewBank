package newbank.server;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Customer  {
	
	private ArrayList<Account> accounts;
	private String password;
	private String address;
	private String legalName;	
	private String userId;

	
	private double balance;
	public Customer(String userId, double balance) {
		accounts = new ArrayList<>();
		this.balance = balance;
		this.userId = userId;
		this.legalName = userId;
	}
	
	public Customer(String userId) {
		accounts = new ArrayList<>();
		this.userId = userId;
		this.balance = 0.0; // Set a default value for balance
	}

	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}

	public Boolean checkPassword(String pass){
		if(this.password==pass)
		{return true;}
		else
		{return false;}
	}

	public void addAddress(String address){
		this.address=address;
	}
	public void addName(String name){
		this.legalName = name;
	}
	public String getAddress(){
		return this.address;
	}
	public String getName(){
		return this.legalName;
	}
	public void changePassword(String pass){
		this.password = pass;
	}
	public void addAccount(Account account) {
		accounts.add(account);		
	}
	

	public void storeCustomerData(String userId) {
    try {
        File directory = new File("customerData");
        if (!directory.exists()) {
            directory.mkdir();
        }

        String fileName = "customerData/" + userId + ".txt";

        FileWriter fileWriter = new FileWriter(fileName);
		// store the values in this particular order
        fileWriter.write(userId + "\n" +  this.legalName +"\n" +  this.balance);
        fileWriter.close();

        System.out.println("Customer data stored successfully.");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void printCustomerData() {
	System.out.println("\n------------------------------------------------------------------------------------------------------------------------");

    System.out.println("User: " + userId + " Balance: " + balance);
	System.out.println("------------------------------------------------------------------------------------------------------------------------\n");

}


	
public static Customer loadCustomerData(String userId) {
    try {
        String fileName = "customerData/" + userId + ".txt";
        File file = new File(fileName);

        if (file.exists()) {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
// reading the values in the same order as they are stored
            String loadedUserId = bufferedReader.readLine();
			
			String legalName = (bufferedReader.readLine());
            
			double loadedBalance = Double.parseDouble(bufferedReader.readLine());    


            fileReader.close();

            System.out.println("Customer data loaded successfully.");
            return new Customer(loadedUserId, loadedBalance);
        } else {
            System.out.println("Customer data not found. Creating a new customer.");
            Customer customer = new Customer(""); // Create a new customer object
         	 customer.legalName = userId;
				customer.storeCustomerData(userId); // Save the new customer data
            return customer;
        }
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
	
}
		
	public void deposit(double amount) {
		this.balance += amount;
	}
	
	public void withdraw(double amount) {
		if (amount <= this.balance) {
			this.balance -= amount;
		} else {
			System.out.println("Insufficient funds.");
		}
	}
}
