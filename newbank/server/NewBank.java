package newbank.server;

import java.util.HashMap;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	
	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}
	
	private void addTestData() {

		//Passwords added to allow code to run AP
		// Customer bhagy = new Customer("bhagyPassword");
		// bhagy.addAccount(new Account("Main", 1000.0));
		// customers.put("Bhagy", bhagy);
		
		// Customer christina = new Customer("christinaPassword");
		// christina.addAccount(new Account("Savings", 1500.0));
		// customers.put("Christina", christina);

		// Customer john = new Customer("johnPassword");
		// john.addAccount(new Account("Checking", 250.0));
		// customers.put("John", john);
	}
	
	public static NewBank getBank() {
		return bank;
	}
	
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {
			case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
			default : return "FAIL";
			}
		}
		return "FAIL";
	}
	
	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

}
