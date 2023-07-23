package newbank.server;

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

}
