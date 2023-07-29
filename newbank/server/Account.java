package newbank.server;

import java.io.File;
import java.io.FileWriter;

public abstract class Account {
	
	private String accountName;
	private double balance;
	private String accountType;

	public Account(String accountName, String accountType, double balance) {
		this.accountName = accountName;
		this.balance = balance;
		this.accountType = accountType;
	}
	
	public void deposit( double amount){
		this.balance += amount;
	}
	public void withdraw(double amount){
		if (amount <= this.balance) {
			this.balance -= amount;
		} else {
			System.out.println("Insufficient funds.");
		}
	}
	public void move(double amount, Account a){
		this.withdraw(amount);
		a.deposit(amount);
	}

	public double getBalance(){
		return balance;
	}

	public String toString() {
		return (accountType + "Account: " + accountName + " - Balance " + balance);
	}

	// storing customer account data in the customerData directory
	public void storeCustomerAccount(String userId) {
		
    try {
        File directory = new File("customerData");
        if (!directory.exists()) {
            directory.mkdir();
        }

        String fileName = "customerData/" + userId +this.accountName+ ".txt";

        FileWriter fileWriter = new FileWriter(fileName);
		// store the values in this particular order
        fileWriter.write(userId + "\n" + accountName +"\n"+this.accountType+ "\n" +  this.balance);
        fileWriter.close();

        System.out.println("Customer account data stored successfully.");
    } catch (Exception e) {
        e.printStackTrace();
    	}
	}
	public abstract Account loadCustomerAccount(String userId, String accountName,String accountType);


}
