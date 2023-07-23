package newbank.server;

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
}
