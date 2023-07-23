package newbank.server;

public class CheckingAccount extends Account {

    public CheckingAccount(String accountName, double balance){
        super(accountName, "Checking", balance);
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
        return "Checking Account: " + getBalance();
    }
}
