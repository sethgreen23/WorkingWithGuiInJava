
public class Bank {
	private int account=100;
	
	public int getAccount() {
		return account;
	}
	
	public void withdraw(int amount) {
		account-=amount;
	}
}
